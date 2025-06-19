package cool.scx.data;

import cool.scx.data.exception.DataAccessException;
import cool.scx.functional.ScxCallable;
import cool.scx.functional.ScxRunnable;

public interface ContextManager {

    <T, E extends Throwable> T withContext(ScxCallable<T, E> handler) throws DataAccessException, E;

    <E extends Throwable> void withContext(ScxRunnable<E> handler) throws DataAccessException, E;

}
