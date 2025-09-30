package cool.scx.jdbc.sql;


import cool.scx.function.Function0;
import cool.scx.function.Function0Void;
import cool.scx.function.Function1;
import cool.scx.function.Function1Void;
import cool.scx.jdbc.JDBCContext;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.result_handler.ResultHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_FORWARD_ONLY;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

/// SQLRunner 用于执行简单的 jdbc 查询
///
/// @author scx567888
/// @version 0.0.1
public final class SQLRunner {

    private static final ScopedValue<Connection> CONNECTION_SCOPE_VALUE = ScopedValue.newInstance();

    private static final ScopedValue<SQLRunner> SQL_RUNNER_SCOPE_VALUE = ScopedValue.newInstance();

    private final JDBCContext jdbcContext;

    public SQLRunner(JDBCContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    private static List<Long> getGeneratedKeys(PreparedStatement preparedStatement) throws SQLException {
        try (var resultSet = preparedStatement.getGeneratedKeys()) {
            var ids = new ArrayList<Long>();
            while (resultSet.next()) {
                ids.add(resultSet.getLong(1));
            }
            return ids;
        }
    }

    private static PreparedStatement fillParams(SQL sql, PreparedStatement preparedStatement, Dialect typeHandlerSelector) throws SQLException {
        return sql.isBatch() ? fillBatch(sql, preparedStatement, typeHandlerSelector) : fillSingle(sql, preparedStatement, typeHandlerSelector);
    }

    private static PreparedStatement fillSingle(SQL sql, PreparedStatement preparedStatement, Dialect typeHandlerSelector) throws SQLException {
        //单条数据
        if (sql.params() != null) {
            fillPreparedStatement(preparedStatement, sql.params(), typeHandlerSelector);
        }
        return preparedStatement;
    }

    private static PreparedStatement fillBatch(SQL sql, PreparedStatement preparedStatement, Dialect typeHandlerSelector) throws SQLException {
        if (sql.batchParams() != null) {
            //根据数据量, 判断是否使用 批量插入
            for (var paramArray : sql.batchParams()) {
                if (paramArray != null) {
                    fillPreparedStatement(preparedStatement, paramArray, typeHandlerSelector);
                    preparedStatement.addBatch();
                }
            }
        }
        return preparedStatement;
    }

    private static void fillPreparedStatement(PreparedStatement preparedStatement, Object[] params, Dialect dialect) throws SQLException {
        var index = 1;
        for (var tempValue : params) {
            if (tempValue == null) {
                //这里的 Types.NULL 其实内部并没有使用
                preparedStatement.setNull(index, Types.NULL);
            } else {
                var typeHandler = dialect.findTypeHandler(tempValue.getClass());
                typeHandler.setObject(preparedStatement, index, tempValue);
            }
            index = index + 1;
        }
    }

    private Connection getConnection() throws SQLException {
        return getConnection(true);
    }

    private Connection getConnection(boolean autoCommit) throws SQLException {
        var con = jdbcContext.dataSource().getConnection();
        con.setAutoCommit(autoCommit);
        return con;
    }

    public <T, X extends Throwable> T query(Connection con, SQL sql, ResultHandler<T, X> resultHandler) throws SQLException, X {
        try (var preparedStatement = con.prepareStatement(sql.sql(), TYPE_FORWARD_ONLY, CONCUR_READ_ONLY)) {
            fillParams(sql, preparedStatement, jdbcContext.dialect());
            jdbcContext.dialect().beforeExecuteQuery(preparedStatement);
            var resultSet = preparedStatement.executeQuery();
            return resultHandler.apply(resultSet, jdbcContext.dialect());
        }
    }

    public long execute(Connection con, SQL sql) throws SQLException {
        try (var preparedStatement = con.prepareStatement(sql.sql(), RETURN_GENERATED_KEYS)) {
            fillParams(sql, preparedStatement, jdbcContext.dialect());
            preparedStatement.execute();
            return preparedStatement.getLargeUpdateCount();
        }
    }

    public UpdateResult update(Connection con, SQL sql) throws SQLException {
        try (var preparedStatement = con.prepareStatement(sql.sql(), RETURN_GENERATED_KEYS)) {
            fillParams(sql, preparedStatement, jdbcContext.dialect());
            var affectedItemsCount = preparedStatement.executeLargeUpdate();
            var generatedKeys = getGeneratedKeys(preparedStatement);
            return new UpdateResult(affectedItemsCount, generatedKeys);
        }
    }

    public UpdateResult updateBatch(Connection con, SQL sql) throws SQLException {
        try (var preparedStatement = con.prepareStatement(sql.sql(), RETURN_GENERATED_KEYS)) {
            fillParams(sql, preparedStatement, jdbcContext.dialect());
            var counts = preparedStatement.executeLargeBatch();
            long affectedItemsCount = 0;
            for (long count : counts) {
                affectedItemsCount += count;
            }
            var generatedKeys = getGeneratedKeys(preparedStatement);
            return new UpdateResult(affectedItemsCount, generatedKeys);
        }
    }

    /// query (自动管理连接)
    public <T, X extends Throwable> T query(SQL sql, ResultHandler<T, X> resultHandler) throws SQLRunnerException, X {
        try {
            // 我们根据 CONNECTION_SCOPE_VALUE 来判断是否处于 autoTransaction 中
            if (CONNECTION_SCOPE_VALUE.isBound()) {
                var connection = CONNECTION_SCOPE_VALUE.get();
                return query(connection, sql, resultHandler);
            } else {
                // 我们根据 SQL_RUNNER_SCOPE_VALUE 来判断是否处于 另一个上下文 中
                var sqlRunner = SQL_RUNNER_SCOPE_VALUE.isBound() ? SQL_RUNNER_SCOPE_VALUE.get() : this;
                try (var con = sqlRunner.getConnection()) {
                    return query(con, sql, resultHandler);
                }
            }
        } catch (SQLException sqlException) {
            throw new SQLRunnerException(sqlException);
        }
    }

    /// execute (自动管理连接)
    public long execute(SQL sql) throws SQLRunnerException {
        try {
            // 我们根据 CONNECTION_SCOPE_VALUE 来判断是否处于 autoTransaction 中
            if (CONNECTION_SCOPE_VALUE.isBound()) {
                var connection = CONNECTION_SCOPE_VALUE.get();
                return execute(connection, sql);
            } else {
                // 我们根据 SQL_RUNNER_SCOPE_VALUE 来判断是否处于 另一个上下文 中
                var sqlRunner = SQL_RUNNER_SCOPE_VALUE.isBound() ? SQL_RUNNER_SCOPE_VALUE.get() : this;
                try (var con = sqlRunner.getConnection()) {
                    return execute(con, sql);
                }
            }
        } catch (SQLException sqlException) {
            throw new SQLRunnerException(sqlException);
        }
    }

    /// update (自动管理连接)
    public UpdateResult update(SQL sql) throws SQLRunnerException {
        try {
            // 我们根据 CONNECTION_SCOPE_VALUE 来判断是否处于 autoTransaction 中
            if (CONNECTION_SCOPE_VALUE.isBound()) {
                var connection = CONNECTION_SCOPE_VALUE.get();
                return update(connection, sql);
            } else {
                // 我们根据 SQL_RUNNER_SCOPE_VALUE 来判断是否处于 另一个上下文 中
                var sqlRunner = SQL_RUNNER_SCOPE_VALUE.isBound() ? SQL_RUNNER_SCOPE_VALUE.get() : this;
                try (var con = sqlRunner.getConnection()) {
                    return update(con, sql);
                }
            }
        } catch (SQLException sqlException) {
            throw new SQLRunnerException(sqlException);
        }
    }

    /// updateBatch (自动管理连接)
    public UpdateResult updateBatch(SQL sql) throws SQLRunnerException {
        try {
            // 我们根据 CONNECTION_SCOPE_VALUE 来判断是否处于 autoTransaction 中
            if (CONNECTION_SCOPE_VALUE.isBound()) {
                var connection = CONNECTION_SCOPE_VALUE.get();
                return updateBatch(connection, sql);
            } else {
                // 我们根据 SQL_RUNNER_SCOPE_VALUE 来判断是否处于 另一个上下文 中
                var sqlRunner = SQL_RUNNER_SCOPE_VALUE.isBound() ? SQL_RUNNER_SCOPE_VALUE.get() : this;
                try (var con = sqlRunner.getConnection()) {
                    return updateBatch(con, sql);
                }
            }
        } catch (SQLException sqlException) {
            throw new SQLRunnerException(sqlException);
        }
    }

    /// 自动处理事务并在产生异常时进行自动回滚
    public <T, X extends Throwable> T autoTransaction(Function0<T, X> handler) throws X, SQLRunnerException {
        try (var con = getConnection(false)) {
            return ScopedValue.where(CONNECTION_SCOPE_VALUE, con).call(() -> {
                T result;
                try {
                    //尝试执行业务逻辑
                    result = handler.apply();
                } catch (Throwable handlerE) {
                    // 业务异常发生, 尝试回滚事务
                    try {
                        con.rollback();
                    } catch (SQLException rollbackE) {
                        // 回滚失败, 将业务异常作为附加异常抛出
                        rollbackE.addSuppressed(handlerE);
                        throw new SQLRunnerException("Rollback failed after business exception", rollbackE);
                    }
                    // 回滚成功, 抛出业务异常
                    throw handlerE;
                }

                try {
                    //提交事务
                    con.commit();
                } catch (SQLException commitE) {
                    try {
                        con.rollback();
                    } catch (SQLException rollbackE) {
                        // 回滚失败, 优先抛出回滚异常, 同时附加提交异常
                        rollbackE.addSuppressed(commitE);
                        throw new SQLRunnerException("Rollback failed after commit failure", rollbackE);
                    }
                    throw new SQLRunnerException("Commit failed", commitE);
                }

                return result;

            });
        } catch (SQLException e) {
            throw new SQLRunnerException(e);
        }
    }

    /// 自动处理事务并在产生异常时进行自动回滚
    public <X extends Throwable> void autoTransaction(Function0Void<X> handler) throws X, SQLRunnerException {
        autoTransaction(() -> {
            handler.apply();
            return null;
        });
    }

    /// 需要用户手动提交事务
    public <T, X extends Throwable> T withTransaction(Function1<Connection, T, X> handler) throws SQLRunnerException, X {
        try (var con = getConnection(false)) {
            return ScopedValue.where(CONNECTION_SCOPE_VALUE, con).call(() -> handler.apply(con));
        } catch (SQLException e) {
            throw new SQLRunnerException(e);
        }
    }

    public <X extends Throwable> void withTransaction(Function1Void<Connection, X> handler) throws SQLRunnerException, X {
        withTransaction((con) -> {
            handler.apply(con);
            return null;
        });
    }

    /// 更改上下文
    public <T, X extends Throwable> T autoContext(Function0<T, X> handler) throws SQLRunnerException, X {
        return ScopedValue.where(SQL_RUNNER_SCOPE_VALUE, this).call(handler::apply);
    }

    public <X extends Throwable> void autoContext(Function0Void<X> handler) throws SQLRunnerException, X {
        autoContext(() -> {
            handler.apply();
            return null;
        });
    }

}
