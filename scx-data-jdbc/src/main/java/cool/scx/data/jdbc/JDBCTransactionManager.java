package cool.scx.data.jdbc;

import cool.scx.data.context.TransactionManager;
import cool.scx.data.exception.DataAccessException;
import cool.scx.function.Function0;
import cool.scx.function.Function0Void;
import cool.scx.function.Function1;
import cool.scx.function.Function1Void;
import cool.scx.jdbc.JDBCContext;
import cool.scx.jdbc.sql.SQLRunnerException;

public class JDBCTransactionManager implements TransactionManager<JDBCTransactionContext> {

    private final JDBCContext jdbcContext;

    public JDBCTransactionManager(JDBCContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    @Override
    public <T, X extends Throwable> T withTransaction(Function1<JDBCTransactionContext, T, X> handler) throws DataAccessException, X {
        try {
            return jdbcContext.sqlRunner().withTransaction((con) -> {
                return handler.apply(new JDBCTransactionContext(con));
            });
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <X extends Throwable> void withTransaction(Function1Void<JDBCTransactionContext, X> handler) throws DataAccessException, X {
        try {
            jdbcContext.sqlRunner().withTransaction((con) -> {
                handler.apply(new JDBCTransactionContext(con));
            });
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <T, X extends Throwable> T autoTransaction(Function0<T, X> handler) throws DataAccessException, X {
        try {
            return jdbcContext.sqlRunner().autoTransaction(handler);
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <X extends Throwable> void autoTransaction(Function0Void<X> handler) throws DataAccessException, X {
        try {
            jdbcContext.sqlRunner().autoTransaction(handler);
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <T, X extends Throwable> T autoContext(Function0<T, X> handler) throws DataAccessException, X {
        try {
            return jdbcContext.sqlRunner().autoContext(handler);
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <X extends Throwable> void autoContext(Function0Void<X> handler) throws DataAccessException, X {
        try {
            jdbcContext.sqlRunner().autoContext(handler);
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

}
