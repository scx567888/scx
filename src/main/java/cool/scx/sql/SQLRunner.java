package cool.scx.sql;

import cool.scx.ScxHandlerE;
import cool.scx.ScxHandlerRE;
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
     * 数据源
     */
    private final DataSource dataSource;

    /**
     * 根据数据源构建一个 SQLRunner
     *
     * @param dataSource a DataSource object
     */
    public SQLRunner(DataSource dataSource) {
        if (dataSource != null) {
            this.dataSource = dataSource;
        } else {
            throw new IllegalArgumentException("数据源不能为空 !!!");
        }
    }

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

    public <T> T query(AbstractPlaceholderSQL<?> placeholderSQL, ScxHandlerRE<ResultSet, T, SQLException> resultSetHandler) {
        return ScxExceptionHelper.wrap(() -> {
            try (var con = dataSource.getConnection()) {
                return query(con, placeholderSQL, resultSetHandler);
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
            try (var con = dataSource.getConnection()) {
                return execute(con, placeholderSQL);
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
            try (var con = dataSource.getConnection()) {
                return update(con, placeholderSQL);
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
            try (var con = dataSource.getConnection()) {
                return updateBatch(con, placeholderSQL);
            }
        });
    }

    /**
     * 自动处理事务并在产生异常时进行自动回滚
     * 用法
     * <pre>{@code
     *      假设有以下结构的数据表
     *      create table test (
     *          name varchar(32) null unique,
     *      );
     *      在连接消费者中传入要执行的操作
     *      try {
     *          sqlRunner.autoTransaction(con -> {
     *              // 这句代码会正确执行
     *              SQLRunner.execute(con, "insert into test(name) values('uniqueName') ", null);
     *              // 这句会产生异常 这时上一个语句会进行回滚 (rollback) 同时将异常抛出
     *              SQLRunner.execute(con, "insert into test(name) values('uniqueName') ", null);
     *          });
     *       } catch (Exception e) {
     *          //这里会捕获 getConnection 可能产生的 SQLException 和 autoTransaction 代码块中产生的所有异常
     *          e.printStackTrace();
     *      }
     *  }</pre>
     *
     * @param handler 连接消费者
     * @throws java.lang.Exception if any.
     */
    public void autoTransaction(ScxHandlerE<Connection, Exception> handler) throws Exception {
        try (var con = dataSource.getConnection()) {
            autoTransaction(con, handler);
        }
    }

    /**
     * a
     *
     * @param handler a
     * @param <T>     a
     * @return a
     * @throws Exception a
     */
    public <T> T autoTransaction(ScxHandlerRE<Connection, T, Exception> handler) throws Exception {
        try (var con = dataSource.getConnection()) {
            return autoTransaction(con, handler);
        }
    }

}
