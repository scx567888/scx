package cool.scx.data;

import cool.scx.data.exception.DataAccessException;
import cool.scx.functional.ScxCallable;
import cool.scx.functional.ScxConsumer;
import cool.scx.functional.ScxFunction;
import cool.scx.functional.ScxRunnable;

public interface TransactionManager<C extends TransactionContext> extends DataManager<C> {

    C createTransactionContext();

    <T, E extends Throwable> T withTransaction(ScxFunction<C, T, E> handler) throws DataAccessException, E;

    <E extends Throwable> void withTransaction(ScxConsumer<C, E> handler) throws DataAccessException, E;

    <T, E extends Throwable> T autoTransaction(ScxCallable<T, E> handler) throws DataAccessException, E;

    <E extends Throwable> void autoTransaction(ScxRunnable<E> handler) throws DataAccessException, E;

}
