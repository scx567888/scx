package cool.scx.sql;

import cool.scx.functional.ScxConsumer;
import cool.scx.functional.ScxFunction;
import cool.scx.functional.ScxRunnable;
import cool.scx.util.ArrayUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

import static cool.scx.util.ScxExceptionHelper.wrap;

/**
 * SQLRunner 执行 cool.scx.sql 语句
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class SQLRunner {

    /**
     * a
     */
    private static final InheritableThreadLocal<Connection> CONNECTION_THREAD_LOCAL = new InheritableThreadLocal<>();

    /**
     * 数据源
     */
    private final DataSource dataSource;

    /**
     * 根据数据源构建一个 SQLRunner
     *
     * @param dataSource a DataSource object
     */
    public SQLRunner(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("数据源不能为空 !!!");
        }
        this.dataSource = dataSource;
    }

    /**
     * a
     *
     * @param con           a
     * @param sql           a
     * @param resultHandler a
     * @param <T>           a
     * @return a
     * @throws SQLException a
     */
    public static <T> T query(Connection con, SQL sql, ResultHandler<T> resultHandler) throws SQLException {
        try (var preparedStatement = sql.prepareStatement(con)) {
            var resultSet = preparedStatement.executeQuery();
            return resultHandler.apply(resultSet);
        }
    }

    /**
     * a
     *
     * @param con a
     * @param sql a
     * @return a
     * @throws SQLException a
     */
    public static long execute(Connection con, SQL sql) throws SQLException {
        try (var preparedStatement = sql.prepareStatement(con)) {
            preparedStatement.execute();
            return preparedStatement.getLargeUpdateCount();
        }
    }

    /**
     * a
     *
     * @param con a
     * @param sql a
     * @return a
     * @throws SQLException a
     */
    public static UpdateResult update(Connection con, SQL sql) throws SQLException {
        try (var preparedStatement = sql.prepareStatement(con)) {
            var affectedItemsCount = preparedStatement.executeLargeUpdate();
            var generatedKeys = getGeneratedKeys(preparedStatement);
            return new UpdateResult(affectedItemsCount, generatedKeys);
        }
    }

    /**
     * 批量执行更新语句
     *
     * @param sql cool.scx.sql
     * @param con a Connection object
     * @return r
     * @throws SQLException if any.
     */
    public static UpdateResult updateBatch(Connection con, SQL sql) throws SQLException {
        try (var preparedStatement = sql.prepareStatement(con)) {
            var affectedItemsCount = preparedStatement.executeLargeBatch().length;
            var generatedKeys = getGeneratedKeys(preparedStatement);
            return new UpdateResult(affectedItemsCount, generatedKeys);
        }
    }

    /**
     * 同 autoTransaction
     *
     * @param con     连接对象
     * @param handler handler
     * @throws Exception e
     */
    public static void autoTransaction(Connection con, ScxConsumer<Connection, Exception> handler) throws Exception {
        con.setAutoCommit(false);
        try {
            handler.accept(con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        }
    }

    /**
     * a
     *
     * @param con     a
     * @param handler a
     * @param <T>     a
     * @return a
     * @throws Exception a
     */
    public static <T> T autoTransaction(Connection con, ScxFunction<Connection, T, Exception> handler) throws Exception {
        con.setAutoCommit(false);
        try {
            T result = handler.apply(con);
            con.commit();
            return result;
        } catch (Exception e) {
            con.rollback();
            throw e;
        }
    }

    /**
     * <p>getUpdateResult.</p>
     *
     * @param preparedStatement a PreparedStatement object
     * @return a {@link cool.scx.sql.UpdateResult} object
     * @throws SQLException if any.
     */
    private static long[] getGeneratedKeys(PreparedStatement preparedStatement) throws SQLException {
        try (var resultSet = preparedStatement.getGeneratedKeys()) {
            var ids = new ArrayList<Long>();
            while (resultSet.next()) {
                ids.add(resultSet.getLong(1));
            }
            return ArrayUtils.toPrimitive(ids);
        }
    }

    /**
     * a
     *
     * @param sql           a
     * @param resultHandler a
     * @param <T>           a
     * @return a
     */
    public <T> T query(SQL sql, ResultHandler<T> resultHandler) {
        return wrap(() -> {
            //我们根据 CONNECTION_THREAD_LOCAL.get() 是否为 null 来判断是否处于 autoTransaction 中
            var connection = CONNECTION_THREAD_LOCAL.get();
            if (connection != null) {
                return query(connection, sql, resultHandler);
            } else {
                try (var con = getConnection()) {
                    return query(con, sql, resultHandler);
                }
            }
        });
    }

    /**
     * 执行 cool.scx.sql 语句
     *
     * @param sql a {@link String} object.
     * @return a 执行结果
     */
    public long execute(SQL sql) {
        return wrap(() -> {
            //我们根据 CONNECTION_THREAD_LOCAL.get() 是否为 null 来判断是否处于 autoTransaction 中
            var connection = CONNECTION_THREAD_LOCAL.get();
            if (connection != null) {
                return execute(connection, sql);
            } else {
                try (var con = getConnection()) {
                    return execute(con, sql);
                }
            }
        });
    }

    /**
     * a
     *
     * @param sql a
     * @return a
     */
    public UpdateResult update(SQL sql) {
        return wrap(() -> {
            //我们根据 CONNECTION_THREAD_LOCAL.get() 是否为 null 来判断是否处于 autoTransaction 中
            var connection = CONNECTION_THREAD_LOCAL.get();
            if (connection != null) {
                return update(connection, sql);
            } else {
                try (var con = getConnection()) {
                    return update(con, sql);
                }
            }
        });
    }

    /**
     * 批量执行更新语句
     *
     * @param sql a
     * @return a
     */
    public UpdateResult updateBatch(SQL sql) {
        return wrap(() -> {
            //我们根据 CONNECTION_THREAD_LOCAL.get() 是否为 null 来判断是否处于 autoTransaction 中
            var connection = CONNECTION_THREAD_LOCAL.get();
            if (connection != null) {
                return updateBatch(connection, sql);
            } else {
                try (var con = getConnection()) {
                    return updateBatch(con, sql);
                }
            }
        });
    }

    /**
     * 自动处理事务并在产生异常时进行自动回滚
     * 注意 其中的操作会在另一个线程中执行 所以需要注意线程的操作
     * 当抛出异常时 请使用 {@link cool.scx.util.ScxExceptionHelper#getRootCause(Throwable)} 来获取真正的异常
     * 用法
     * <pre>{@code
     *      假设有以下结构的数据表
     *      create table test (
     *          name varchar(32) null unique,
     *      );
     *      在连接消费者中传入要执行的操作
     *      SQLRunner sqlRunner = xxx;
     *         try {
     *             sqlRunner.autoTransaction(() -> {
     *                 // 这句代码会正确执行
     *                 sqlRunner.execute(NoParametersSQL.of("insert into test(name) values('uniqueName') "));
     *                 // 这句会产生异常 这时上一个语句会进行回滚 (rollback) 同时将异常抛出
     *                 sqlRunner.execute(NoParametersSQL.of("insert into test(name) values('uniqueName') "));
     *             });
     *         } catch (Exception e) {
     *             //这里会捕获 getConnection 可能产生的 SQLException 和 autoTransaction 代码块中产生的所有异常
     *             //因为会进行多层包裹 所以建议使用 ScxExceptionHelper.getRootCause(e); 来获取真正的异常
     *             ScxExceptionHelper.getRootCause(e).printStackTrace();
     *         }
     *  }</pre>
     *
     * @param handler 连接消费者
     */
    public void autoTransaction(ScxRunnable<?> handler) {
        wrap(() -> ForkJoinPool.commonPool().submit(() -> {
            try (var con = getConnection(false)) {
                CONNECTION_THREAD_LOCAL.set(con);
                try {
                    handler.run();
                    con.commit();
                    return null;
                } catch (Exception e) {
                    con.rollback();
                    throw e;
                } finally {
                    CONNECTION_THREAD_LOCAL.remove();
                }
            }
        }).get());
    }

    /**
     * 同上 {@link SQLRunner#autoTransaction(ScxRunnable)} 但是有返回值
     *
     * @param handler a
     * @param <T>     a
     * @return a
     */
    public <T> T autoTransaction(Callable<T> handler) {
        return wrap(() -> ForkJoinPool.commonPool().submit(() -> {
            try (var con = getConnection(false)) {
                CONNECTION_THREAD_LOCAL.set(con);
                try {
                    T result = handler.call();
                    con.commit();
                    return result;
                } catch (Exception e) {
                    con.rollback();
                    throw e;
                } finally {
                    CONNECTION_THREAD_LOCAL.remove();
                }
            }
        }).get());
    }

    /**
     * a
     *
     * @return a
     * @throws SQLException a
     */
    private Connection getConnection() throws SQLException {
        return getConnection(true);
    }

    /**
     * a
     *
     * @param autoCommit a
     * @return a
     * @throws SQLException a
     */
    private Connection getConnection(boolean autoCommit) throws SQLException {
        var con = dataSource.getConnection();
        con.setAutoCommit(autoCommit);
        return con;
    }

}
