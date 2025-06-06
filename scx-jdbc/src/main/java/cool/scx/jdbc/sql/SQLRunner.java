package cool.scx.jdbc.sql;

import cool.scx.common.functional.ScxCallable;
import cool.scx.common.functional.ScxConsumer;
import cool.scx.common.functional.ScxFunction;
import cool.scx.common.functional.ScxRunnable;
import cool.scx.jdbc.JDBCContext;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.result_handler.ResultHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import static cool.scx.common.exception.ScxExceptionHelper.wrap;
import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_FORWARD_ONLY;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

/// SQLRunner 用于执行简单的 jdbc 查询
///
/// @author scx567888
/// @version 0.0.1
public final class SQLRunner {

    /// 此处采用线程的方式实现事务隔离 此字段用于存储 不同线程对应的 Connection 连接
    private static final InheritableThreadLocal<Connection> CONNECTION_THREAD_LOCAL = new InheritableThreadLocal<>();
    private final AtomicLong threadNumber = new AtomicLong(0);
    private final JDBCContext jdbcContext;

    public SQLRunner(JDBCContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    /// 自动事务
    ///
    /// @param con     con
    /// @param handler handler
    /// @throws Exception e
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

    /// 自动事务 (带返回值)
    ///
    /// @param con     con
    /// @param handler handler
    /// @param <T>     T
    /// @return 返回值
    /// @throws Exception e
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

    /// query
    ///
    /// @param con           con
    /// @param sql           sql
    /// @param resultHandler resultHandler
    /// @param <T>           T
    /// @return result
    /// @throws SQLException e
    public <T> T query(Connection con, SQL sql, ResultHandler<T> resultHandler) throws SQLException {
        try (var preparedStatement = con.prepareStatement(sql.sql(), TYPE_FORWARD_ONLY, CONCUR_READ_ONLY)) {
            fillParams(sql, preparedStatement, jdbcContext.dialect());
            jdbcContext.dialect().beforeExecuteQuery(preparedStatement);
            var resultSet = preparedStatement.executeQuery();
            return resultHandler.apply(resultSet, jdbcContext.dialect());
        }
    }

    /// query (自动管理连接)
    ///
    /// @param sql           sql
    /// @param resultHandler resultHandler
    /// @param <T>           T
    /// @return result
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

    /// execute
    ///
    /// @param con con
    /// @param sql sql
    /// @return 受影响的行数
    /// @throws SQLException e
    public long execute(Connection con, SQL sql) throws SQLException {
        try (var preparedStatement = con.prepareStatement(sql.sql(), RETURN_GENERATED_KEYS)) {
            fillParams(sql, preparedStatement, jdbcContext.dialect());
            preparedStatement.execute();
            return preparedStatement.getLargeUpdateCount();
        }
    }

    /// execute (自动管理连接)
    ///
    /// @param sql sql
    /// @return 受影响的行数
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

    /// update
    ///
    /// @param con con
    /// @param sql sql
    /// @return UpdateResult
    /// @throws SQLException e
    public UpdateResult update(Connection con, SQL sql) throws SQLException {
        try (var preparedStatement = con.prepareStatement(sql.sql(), RETURN_GENERATED_KEYS)) {
            fillParams(sql, preparedStatement, jdbcContext.dialect());
            var affectedItemsCount = preparedStatement.executeLargeUpdate();
            var generatedKeys = getGeneratedKeys(preparedStatement);
            return new UpdateResult(affectedItemsCount, generatedKeys);
        }
    }

    /// update (自动管理连接)
    ///
    /// @param sql sql
    /// @return UpdateResult
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

    /// updateBatch
    ///
    /// @param con con
    /// @param sql sql
    /// @return UpdateResult
    /// @throws SQLException e
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

    /// updateBatch (自动管理连接)
    ///
    /// @param sql sql
    /// @return UpdateResult
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

    /// 自动处理事务并在产生异常时进行自动回滚
    /// 注意 其中的操作会在另一个线程中执行 所以需要注意线程的操作
    public <E extends Throwable> void autoTransaction(ScxRunnable<E> handler) throws E, TransactionException {
        autoTransaction(() -> {
            handler.run();
            return null;
        });
    }

    /// 自动处理事务并在产生异常时进行自动回滚
    /// 注意 其中的操作会在另一个线程中执行 所以需要注意线程的操作
    @SuppressWarnings("unchecked")
    public <T, E extends Throwable> T autoTransaction(ScxCallable<T, E> handler) throws E, TransactionException {
        var promise = new CompletableFuture<T>();
        Thread.ofVirtual().name("scx-auto-transaction-thread-", threadNumber.getAndIncrement()).start(() -> {

            //这个 try 的功能是配合 CONNECTION_THREAD_LOCAL 和 promise 实现线程的等待和将异常穿透到 promise 
            // 如果 ScopeValue 成熟可移除这种写法 
            try {
                //获取连接
                Connection con;
                try {
                    con = getConnection(false);
                } catch (SQLException e) {
                    throw new TransactionException("Failed to acquire connection", e);
                }

                try (con) {
                    CONNECTION_THREAD_LOCAL.set(con);

                    try {
                        //尝试执行业务逻辑
                        handler.call();
                    } catch (Throwable handlerE) {
                        // 业务异常发生，尝试回滚事务
                        try {
                            con.rollback();
                        } catch (SQLException rollbackE) {
                            // 回滚失败，将业务异常作为附加异常抛出
                            rollbackE.addSuppressed(handlerE);
                            throw new TransactionException("Rollback failed after business exception", rollbackE);
                        }
                        // 回滚成功，抛出业务异常
                        throw handlerE;
                    }

                    try {
                        //提交事务
                        con.commit();
                    } catch (SQLException commitE) {
                        try {
                            con.rollback();
                        } catch (SQLException rollbackE) {
                            // 回滚失败，优先抛出回滚异常，同时附加提交异常
                            rollbackE.addSuppressed(commitE);
                            throw new TransactionException("Rollback failed after commit failure", rollbackE);
                        }
                        throw new TransactionException("Commit failed", commitE);
                    }

                    //通知线程完成
                    promise.complete(null);
                } finally {
                    CONNECTION_THREAD_LOCAL.remove();
                }
            } catch (Throwable e) {
                //通知线程失败
                promise.completeExceptionally(e);
            }
        });

        try {
            return promise.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TransactionException("Transaction thread was interrupted", e);
        } catch (ExecutionException e) {
            //这里理论只有两种可能 一种是 TransactionException 一种是 E 所以这种处理是安全的
            var cause = e.getCause();
            if (cause instanceof TransactionException transactionException) {
                throw transactionException;
            }
            throw (E) cause;
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

}
