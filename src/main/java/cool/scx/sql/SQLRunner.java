package cool.scx.sql;

import cool.scx.ScxHandlerE;
import cool.scx.ScxHandlerRE;
import cool.scx.exception.ScxExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

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

    /**
     * 查询 返回值为 map集合
     *
     * @param sql              a {@link java.lang.String} object.
     * @param param            a {@link java.util.Map} object.
     * @param con              a Connection object
     * @param resultSetHandler a  object
     * @param <T>              a T class
     * @return a map 集合
     * @throws SQLException if any.
     */
    public static <T> T query(Connection con, String sql, ScxHandlerRE<ResultSet, T, SQLException> resultSetHandler, Map<String, Object> param) throws SQLException {
        try (var preparedStatement = new NamedParameterSQL(sql, param).getPreparedStatement(con)) {
            var resultSet = preparedStatement.executeQuery();
            return resultSetHandler.handle(resultSet);
        }
    }

    /**
     * a
     *
     * @param con              a
     * @param sql              a
     * @param resultSetHandler a
     * @param <T>              a
     * @return a
     * @throws SQLException a
     */
    public static <T> T query(Connection con, String sql, ScxHandlerRE<ResultSet, T, SQLException> resultSetHandler, Object... sqlParameters) throws SQLException {
        try (var preparedStatement = new PlaceholderSQL(sql, sqlParameters).getPreparedStatement(con)) {
            var resultSet = preparedStatement.executeQuery();
            return resultSetHandler.handle(resultSet);
        }
    }

    /**
     * 执行 sql 语句
     *
     * @param sql   a {@link java.lang.String} object.
     * @param param a {@link java.util.Map} object.
     * @param con   a Connection object
     * @return a 执行结果
     * @throws SQLException if any.
     */
    public static int execute(Connection con, String sql, Map<String, Object> param) throws SQLException {
        try (var preparedStatement = new NamedParameterSQL(sql, param).getPreparedStatement(con)) {
            preparedStatement.execute();
            return preparedStatement.getUpdateCount();
        }
    }

    public static int execute(Connection con, String sql, Object... sqlParameters) throws SQLException {
        try (var preparedStatement = new PlaceholderSQL(sql, sqlParameters).getPreparedStatement(con)) {
            preparedStatement.execute();
            return preparedStatement.getUpdateCount();
        }
    }

    /**
     * 执行更新语句
     *
     * @param sql   a {@link java.lang.String} object.
     * @param param a {@link java.util.Map} object.
     * @param con   a Connection object
     * @return a {@link cool.scx.sql.UpdateResult} object.
     * @throws SQLException if any.
     */
    public static UpdateResult update(Connection con, String sql, Map<String, Object> param) throws SQLException {
        try (var preparedStatement = new NamedParameterSQL(sql, param).getPreparedStatement(con)) {
            return getUpdateResult(preparedStatement);
        }
    }

    /**
     * a
     *
     * @param con           a
     * @param sql           a
     * @param sqlParameters a
     * @return a
     * @throws SQLException a
     */
    public static UpdateResult update(Connection con, String sql, Object... sqlParameters) throws SQLException {
        try (var preparedStatement = new PlaceholderSQL(sql, sqlParameters).getPreparedStatement(con)) {
            return getUpdateResult(preparedStatement);
        }
    }

    private static UpdateResult getUpdateResult(PreparedStatement preparedStatement) throws SQLException {
        var affectedLength = preparedStatement.executeUpdate();
        var resultSet = preparedStatement.getGeneratedKeys();
        var ids = new ArrayList<Long>();
        while (resultSet.next()) {
            ids.add(resultSet.getLong(1));
        }
        return new UpdateResult(affectedLength, ids);
    }

    /**
     * 批量执行更新语句
     *
     * @param sql          sql
     * @param paramMapList p
     * @param con          a Connection object
     * @return r
     * @throws SQLException if any.
     */
    public static UpdateResult updateBatch(Connection con, String sql, Collection<Map<String, Object>> paramMapList) throws SQLException {
        try (var preparedStatement = new NamedParameterSQL(sql, paramMapList).getPreparedStatement(con)) {
            var affectedLength = preparedStatement.executeBatch().length;
            var resultSet = preparedStatement.getGeneratedKeys();
            var ids = new ArrayList<Long>();
            while (resultSet.next()) {
                ids.add(resultSet.getLong(1));
            }
            return new UpdateResult(affectedLength, ids);
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
     * 查询 返回值为 map集合
     *
     * @param sql              a {@link java.lang.String} object.
     * @param param            a {@link java.util.Map} object.
     * @param resultSetHandler a object
     * @param <T>              a T class
     * @return a map 集合
     */
    public <T> T query(String sql, ScxHandlerRE<ResultSet, T, SQLException> resultSetHandler, Map<String, Object> param) {
        return ScxExceptionHelper.wrap(() -> {
            try (var con = dataSource.getConnection()) {
                return query(con, sql, resultSetHandler, param);
            }
        });
    }

    /**
     * a
     *
     * @param sql              a
     * @param resultSetHandler a
     * @param sqlParameters    a
     * @param <T>              a
     * @return a
     */
    public <T> T query(String sql, ScxHandlerRE<ResultSet, T, SQLException> resultSetHandler, Object... sqlParameters) {
        return ScxExceptionHelper.wrap(() -> {
            try (var con = dataSource.getConnection()) {
                return query(con, sql, resultSetHandler, sqlParameters);
            }
        });
    }

    /**
     * 执行 sql 语句
     *
     * @param sql   a {@link java.lang.String} object.
     * @param param a {@link java.util.Map} object.
     * @return a 执行结果
     */
    public int execute(String sql, Map<String, Object> param) {
        return ScxExceptionHelper.wrap(() -> {
            try (var con = dataSource.getConnection()) {
                return execute(con, sql, param);
            }
        });
    }

    /**
     * a
     *
     * @param sql           a
     * @param sqlParameters a
     * @return a
     */
    public int execute(String sql, Object... sqlParameters) {
        return ScxExceptionHelper.wrap(() -> {
            try (var con = dataSource.getConnection()) {
                return execute(con, sql, sqlParameters);
            }
        });
    }

    /**
     * 执行更新语句
     *
     * @param sql   a {@link java.lang.String} object.
     * @param param a {@link java.util.Map} object.
     * @return a {@link cool.scx.sql.UpdateResult} object.
     */
    public UpdateResult update(String sql, Map<String, Object> param) {
        return ScxExceptionHelper.wrap(() -> {
            try (var con = dataSource.getConnection()) {
                return update(con, sql, param);
            }
        });
    }

    /**
     * a
     *
     * @param sql           a
     * @param sqlParameters a
     * @return a
     */
    public UpdateResult update(String sql, Object... sqlParameters) {
        return ScxExceptionHelper.wrap(() -> {
            try (var con = dataSource.getConnection()) {
                return update(con, sql, sqlParameters);
            }
        });
    }

    /**
     * 批量执行更新语句
     *
     * @param sql          sql
     * @param paramMapList p
     * @return r
     */
    public UpdateResult updateBatch(String sql, Collection<Map<String, Object>> paramMapList) {
        return ScxExceptionHelper.wrap(() -> {
            try (var con = dataSource.getConnection()) {
                return updateBatch(con, sql, paramMapList);
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

}
