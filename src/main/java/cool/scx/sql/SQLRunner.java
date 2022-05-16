package cool.scx.sql;

import cool.scx.ScxHandlerE;
import cool.scx.ScxHandlerRE;
import cool.scx.ScxHandlerVE;
import cool.scx.ScxHandlerVRE;
import cool.scx.util.exception.ScxExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SQLRunner 执行 sql 语句
 *
 * @author scx567888
 * @version 1.0.10
 */
public final class SQLRunner {

    /**
     * logger
     */
    static final Logger logger = LoggerFactory.getLogger(SQLRunner.class);

    /**
     * a
     */
    private static final ExecutorService SQL_RUNNER_EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * a
     */
    private static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL = new ThreadLocal<>();

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
     * @param con              a
     * @param placeholderSQL   a
     * @param resultSetHandler a
     * @param <T>              a
     * @return a
     * @throws SQLException a
     */
    public static <T> T query(Connection con, AbstractPlaceholderSQL<?> placeholderSQL, ScxHandlerRE<ResultSet, T, SQLException> resultSetHandler) throws SQLException {
        try (var preparedStatement = placeholderSQL.getPreparedStatement(con)) {
            var resultSet = preparedStatement.executeQuery();
            return resultSetHandler.handle(resultSet);
        }
    }

    /**
     * a
     *
     * @param con            a
     * @param placeholderSQL a
     * @return a
     * @throws java.sql.SQLException a
     */
    public static long execute(Connection con, AbstractPlaceholderSQL<?> placeholderSQL) throws SQLException {
        try (var preparedStatement = placeholderSQL.getPreparedStatement(con)) {
            preparedStatement.execute();
            return preparedStatement.getLargeUpdateCount();
        }
    }

    /**
     * a
     *
     * @param con            a
     * @param placeholderSQL a
     * @return a
     * @throws SQLException a
     */
    public static UpdateResult update(Connection con, AbstractPlaceholderSQL<?> placeholderSQL) throws SQLException {
        try (var preparedStatement = placeholderSQL.getPreparedStatement(con)) {
            var affectedItemsCount = preparedStatement.executeLargeUpdate();
            var generatedKeys = getGeneratedKeys(preparedStatement);
            return new UpdateResult(affectedItemsCount, generatedKeys);
        }
    }

    /**
     * 批量执行更新语句
     *
     * @param placeholderSQL sql
     * @param con            a Connection object
     * @return r
     * @throws java.sql.SQLException if any.
     */
    public static UpdateResult updateBatch(Connection con, AbstractPlaceholderSQL<?> placeholderSQL) throws SQLException {
        try (var preparedStatement = placeholderSQL.getPreparedStatement(con)) {
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
     * @throws java.lang.Exception e
     */
    public static void autoTransaction(Connection con, ScxHandlerE<Connection, Exception> handler) throws Exception {
        con.setAutoCommit(false);
        try {
            handler.handle(con);
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
    public static <T> T autoTransaction(Connection con, ScxHandlerRE<Connection, T, Exception> handler) throws Exception {
        con.setAutoCommit(false);
        try {
            T result = handler.handle(con);
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
     * @throws java.sql.SQLException if any.
     */
    private static List<Long> getGeneratedKeys(PreparedStatement preparedStatement) throws SQLException {
        var resultSet = preparedStatement.getGeneratedKeys();
        var ids = new ArrayList<Long>();
        while (resultSet.next()) {
            ids.add(resultSet.getLong(1));
        }
        return ids;
    }

    /**
     * a
     *
     * @param placeholderSQL   a
     * @param resultSetHandler a
     * @param <T>              a
     * @return a
     */
    public <T> T query(AbstractPlaceholderSQL<?> placeholderSQL, ScxHandlerRE<ResultSet, T, SQLException> resultSetHandler) {
        return ScxExceptionHelper.wrap(() -> {
            //我们根据 CONNECTION_THREAD_LOCAL.get() 是否为 null 来判断是否处于 autoTransaction 中
            var connection = CONNECTION_THREAD_LOCAL.get();
            if (connection != null) {
                return query(connection, placeholderSQL, resultSetHandler);
            } else {
                try (var con = dataSource.getConnection()) {
                    return query(con, placeholderSQL, resultSetHandler);
                }
            }
        });
    }

    /**
     * 执行 sql 语句
     *
     * @param placeholderSQL a {@link java.lang.String} object.
     * @return a 执行结果
     */
    public long execute(AbstractPlaceholderSQL<?> placeholderSQL) {
        return ScxExceptionHelper.wrap(() -> {
            //我们根据 CONNECTION_THREAD_LOCAL.get() 是否为 null 来判断是否处于 autoTransaction 中
            var connection = CONNECTION_THREAD_LOCAL.get();
            if (connection != null) {
                return execute(connection, placeholderSQL);
            } else {
                try (var con = dataSource.getConnection()) {
                    return execute(con, placeholderSQL);
                }
            }
        });
    }

    /**
     * a
     *
     * @param placeholderSQL a
     * @return a
     */
    public UpdateResult update(AbstractPlaceholderSQL<?> placeholderSQL) {
        return ScxExceptionHelper.wrap(() -> {
            //我们根据 CONNECTION_THREAD_LOCAL.get() 是否为 null 来判断是否处于 autoTransaction 中
            var connection = CONNECTION_THREAD_LOCAL.get();
            if (connection != null) {
                return update(connection, placeholderSQL);
            } else {
                try (var con = dataSource.getConnection()) {
                    return update(con, placeholderSQL);
                }
            }
        });
    }

    /**
     * 批量执行更新语句
     *
     * @param placeholderSQL a
     * @return a
     */
    public UpdateResult updateBatch(AbstractPlaceholderSQL<?> placeholderSQL) {
        return ScxExceptionHelper.wrap(() -> {
            //我们根据 CONNECTION_THREAD_LOCAL.get() 是否为 null 来判断是否处于 autoTransaction 中
            var connection = CONNECTION_THREAD_LOCAL.get();
            if (connection != null) {
                return updateBatch(connection, placeholderSQL);
            } else {
                try (var con = dataSource.getConnection()) {
                    return updateBatch(con, placeholderSQL);
                }
            }
        });
    }

    /**
     * 自动处理事务并在产生异常时进行自动回滚
     * 注意 其中的操作会在另一个线程中执行 所以需要注意线程的操作
     * 当抛出异常时 请使用 {@link ScxExceptionHelper#getRootCause(Throwable)} 来获取真正的异常
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
    public void autoTransaction(ScxHandlerVE<?> handler) {
        ScxExceptionHelper.wrap(() -> SQL_RUNNER_EXECUTOR_SERVICE.submit(() -> {
            try (var con = dataSource.getConnection()) {
                CONNECTION_THREAD_LOCAL.set(con);
                con.setAutoCommit(false);
                try {
                    handler.handle();
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
     * 同上 {@link SQLRunner#autoTransaction(ScxHandlerVE)} 但是有返回值
     *
     * @param handler a
     * @param <T>     a
     * @return a
     */
    public <T> T autoTransaction(ScxHandlerVRE<T, ?> handler) {
        return ScxExceptionHelper.wrap(() -> SQL_RUNNER_EXECUTOR_SERVICE.submit(() -> {
            try (var con = dataSource.getConnection()) {
                CONNECTION_THREAD_LOCAL.set(con);
                con.setAutoCommit(false);
                try {
                    T result = handler.handle();
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

}
