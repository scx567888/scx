package cool.scx.sql;

import com.mysql.cj.jdbc.ClientPreparedStatement;
import cool.scx.ScxHandlerE;
import cool.scx.ScxHandlerRE;
import cool.scx.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SQLRunner 执行 sql 语句
 *
 * @author scx567888
 * @version 1.0.10
 */
public final class SQLRunner {

    /**
     * Constant <code>logger</code>
     */
    private static final Logger logger = LoggerFactory.getLogger(SQLRunner.class);

    private final DataSource dataSource;

    /**
     * <p>Constructor for SQLRunner.</p>
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
        try (var preparedStatement = getPreparedStatement(con, new NamedParameterSQL(sql), param)) {
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
        try (var preparedStatement = getPreparedStatement(con, new NamedParameterSQL(sql), param)) {
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
        try (var preparedStatement = getPreparedStatement(con, new NamedParameterSQL(sql), param)) {
            var affectedLength = preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            var ids = new ArrayList<Long>();
            while (resultSet.next()) {
                ids.add(resultSet.getLong(1));
            }
            return new UpdateResult(affectedLength, ids);
        }
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
    public static UpdateResult updateBatch(Connection con, String sql, List<Map<String, Object>> paramMapList) throws SQLException {
        try (var preparedStatement = getPreparedStatement(con, new NamedParameterSQL(sql), paramMapList)) {
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
     * 获取 PreparedStatement (带填充数据)
     *
     * @param con               链接
     * @param namedParameterSQL 包含具名参数的 sql 语句
     * @param paramMap          a {@link java.util.Map} object
     * @return r
     * @throws SQLException e
     */
    private static PreparedStatement getPreparedStatement(Connection con, NamedParameterSQL namedParameterSQL, Map<String, Object> paramMap) throws SQLException {
        var preparedStatement = con.prepareStatement(namedParameterSQL.normalSQL(), Statement.RETURN_GENERATED_KEYS);
        if (paramMap != null) {
            fillPreparedStatementByNamedParameterNameIndex(preparedStatement, namedParameterSQL.namedParameterNameIndex(), paramMap);
        }
        if (logger.isDebugEnabled()) {
            var realSQL = preparedStatement.unwrap(ClientPreparedStatement.class).asSql();
            logger.debug(realSQL);
        }
        return preparedStatement;
    }

    /**
     * 获取 PreparedStatement (带填充数据)
     *
     * @param con               链接
     * @param namedParameterSQL 包含具名参数的 sql 语句
     * @param paramMapList      参数
     * @return r
     * @throws SQLException e
     */
    private static PreparedStatement getPreparedStatement(Connection con, NamedParameterSQL namedParameterSQL, List<Map<String, Object>> paramMapList) throws SQLException {
        var preparedStatement = con.prepareStatement(namedParameterSQL.normalSQL(), Statement.RETURN_GENERATED_KEYS);
        //循环加入
        for (var paramMap : paramMapList) {
            if (paramMap != null) {
                fillPreparedStatementByNamedParameterNameIndex(preparedStatement, namedParameterSQL.namedParameterNameIndex(), paramMap);
                preparedStatement.addBatch();
            }
        }
        if (logger.isDebugEnabled()) {
            var realSQL = preparedStatement.unwrap(ClientPreparedStatement.class).asSql();
            logger.debug(realSQL + "... 额外的 " + (paramMapList.size() - 1) + " 项");
        }
        return preparedStatement;
    }

    /**
     * 使用具名参数向 preparedStatement 填充数据
     *
     * @param preparedStatement       p
     * @param namedParameterNameIndex 具名参数数组
     * @param paramMap                具名参数 map
     * @throws SQLException e
     */
    private static void fillPreparedStatementByNamedParameterNameIndex(PreparedStatement preparedStatement, String[] namedParameterNameIndex, Map<String, Object> paramMap) throws SQLException {
        var index = 1;
        for (var name : namedParameterNameIndex) {
            var tempValue = paramMap.get(name);
            if (tempValue != null) {
                //判断是否为数据库(MySQL)直接支持的数据类型
                var mysqlType = SQLTypeHelper.getMySQLType(tempValue.getClass());
                if (mysqlType != null) {
                    preparedStatement.setObject(index, tempValue, mysqlType);
                } else {//不是则转换为 json 存入
                    preparedStatement.setString(index, ObjectUtils.writeValueAsString(tempValue, ""));
                }
            } else {
                //这里的 Types.NULL 其实内部并没有使用
                preparedStatement.setNull(index, Types.NULL);
            }
            index = index + 1;
        }
    }

    /**
     * 关闭连接 (一般用不到这个方法)
     *
     * @param connection c
     */
    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
     * @throws SQLException if any.
     */
    public <T> T query(String sql, ScxHandlerRE<ResultSet, T, SQLException> resultSetHandler, Map<String, Object> param) throws SQLException {
        try (var con = dataSource.getConnection()) {
            return query(con, sql, resultSetHandler, param);
        }
    }

    /**
     * 执行 sql 语句
     *
     * @param sql   a {@link java.lang.String} object.
     * @param param a {@link java.util.Map} object.
     * @return a 执行结果
     * @throws SQLException if any.
     */
    public int execute(String sql, Map<String, Object> param) throws SQLException {
        try (var con = dataSource.getConnection()) {
            return execute(con, sql, param);
        }
    }

    /**
     * 执行更新语句
     *
     * @param sql   a {@link java.lang.String} object.
     * @param param a {@link java.util.Map} object.
     * @return a {@link cool.scx.sql.UpdateResult} object.
     * @throws SQLException if any.
     */
    public UpdateResult update(String sql, Map<String, Object> param) throws SQLException {
        try (var con = dataSource.getConnection()) {
            return update(con, sql, param);
        }
    }

    /**
     * 批量执行更新语句
     *
     * @param sql          sql
     * @param paramMapList p
     * @return r
     * @throws SQLException if any.
     */
    public UpdateResult updateBatch(String sql, List<Map<String, Object>> paramMapList) throws SQLException {
        try (var con = dataSource.getConnection()) {
            return updateBatch(con, sql, paramMapList);
        }
    }

    /**
     * 自动处理事务并在产生异常时进行自动回滚
     * 用法
     * <pre>{@code
     *      假设有以下结构的数据表
     *      create table test (
     *          name varchar(32) null,
     *          constraint test_name_uindex unique (name)
     *      );
     *      在连接消费者中传入要执行的操作
     *      try {
     *          SQLRunner.autoTransaction(con -> {
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
