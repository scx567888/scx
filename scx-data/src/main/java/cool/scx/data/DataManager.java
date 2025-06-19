package cool.scx.data;

import cool.scx.data.exception.DataAccessException;
import cool.scx.functional.ScxCallable;
import cool.scx.functional.ScxConsumer;
import cool.scx.functional.ScxFunction;
import cool.scx.functional.ScxRunnable;

public interface DataManager<C extends DataContext> {

    C createDataContext();

    <T, E extends Throwable> T withContext(ScxFunction<C, T, E> handler) throws DataAccessException, E;

    <E extends Throwable> void withContext(ScxConsumer<C, E> handler) throws DataAccessException, E;

    <T, E extends Throwable> T autoContext(ScxCallable<T, E> handler) throws DataAccessException, E;

    <E extends Throwable> void autoContext(ScxRunnable<E> handler) throws DataAccessException, E;

}
