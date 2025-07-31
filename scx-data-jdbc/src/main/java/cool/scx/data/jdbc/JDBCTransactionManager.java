package cool.scx.data.jdbc;

import cool.scx.data.context.TransactionManager;
import cool.scx.data.exception.DataAccessException;
import cool.scx.function.CallableX;
import cool.scx.function.ConsumerX;
import cool.scx.function.FunctionX;
import cool.scx.function.RunnableX;
import cool.scx.function.CallableX;
import cool.scx.function.ConsumerX;
import cool.scx.function.FunctionX;
import cool.scx.function.RunnableX;
import cool.scx.jdbc.JDBCContext;
import cool.scx.jdbc.sql.SQLRunnerException;

public class JDBCTransactionManager implements TransactionManager<JDBCTransactionContext> {

    private final JDBCContext jdbcContext;

    public JDBCTransactionManager(JDBCContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    @Override
    public <T, X extends Throwable> T withTransaction(FunctionX<JDBCTransactionContext, T, X> handler) throws DataAccessException, X {
        try {
            return jdbcContext.sqlRunner().withTransaction((con) -> {
                return handler.apply(new JDBCTransactionContext(con));
            });
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <X extends Throwable> void withTransaction(ConsumerX<JDBCTransactionContext, X> handler) throws DataAccessException, X {
        try {
            jdbcContext.sqlRunner().withTransaction((con) -> {
                handler.accept(new JDBCTransactionContext(con));
            });
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <T, X extends Throwable> T autoTransaction(CallableX<T, X> handler) throws DataAccessException, X {
        try {
            return jdbcContext.sqlRunner().autoTransaction(handler);
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <X extends Throwable> void autoTransaction(RunnableX<X> handler) throws DataAccessException, X {
        try {
            jdbcContext.sqlRunner().autoTransaction(handler);
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <T, X extends Throwable> T autoContext(CallableX<T, X> handler) throws DataAccessException, X {
        try {
            return jdbcContext.sqlRunner().autoContext(handler);
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <X extends Throwable> void autoContext(RunnableX<X> handler) throws DataAccessException, X {
        try {
            jdbcContext.sqlRunner().autoContext(handler);
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

}
