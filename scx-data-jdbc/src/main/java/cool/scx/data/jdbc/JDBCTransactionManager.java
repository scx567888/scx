package cool.scx.data.jdbc;

import cool.scx.data.TransactionManager;
import cool.scx.data.exception.DataAccessException;
import cool.scx.functional.ScxCallable;
import cool.scx.functional.ScxConsumer;
import cool.scx.functional.ScxFunction;
import cool.scx.functional.ScxRunnable;
import cool.scx.jdbc.JDBCContext;
import cool.scx.jdbc.sql.SQLRunnerException;

public class JDBCTransactionManager implements TransactionManager<JDBCTransactionContext> {

    private final JDBCContext jdbcContext;

    public JDBCTransactionManager(JDBCContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    @Override
    public <T, E extends Throwable> T withTransaction(ScxFunction<JDBCTransactionContext, T, E> handler) throws DataAccessException, E {
        try {
            return jdbcContext.sqlRunner().withTransaction((con) -> {
                return handler.apply(new JDBCTransactionContext(con));
            });
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <E extends Throwable> void withTransaction(ScxConsumer<JDBCTransactionContext, E> handler) throws DataAccessException, E {
        try {
            jdbcContext.sqlRunner().withTransaction((con) -> {
                handler.accept(new JDBCTransactionContext(con));
            });
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }

    }

    @Override
    public <T, E extends Throwable> T autoTransaction(ScxCallable<T, E> handler) throws DataAccessException, E {
        try {
            return jdbcContext.sqlRunner().autoTransaction(handler);
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <E extends Throwable> void autoTransaction(ScxRunnable<E> handler) throws DataAccessException, E {
        try {
            jdbcContext.sqlRunner().autoTransaction(handler);
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <T, E extends Throwable> T autoContext(ScxCallable<T, E> handler) throws DataAccessException, E {
        try {
            return jdbcContext.sqlRunner().autoContext(handler);
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <E extends Throwable> void autoContext(ScxRunnable<E> handler) throws DataAccessException, E {
        try {
            jdbcContext.sqlRunner().autoContext(handler);
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

}
