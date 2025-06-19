package cool.scx.data;

import cool.scx.data.exception.DataAccessException;
import cool.scx.functional.ScxCallable;
import cool.scx.functional.ScxRunnable;

/// ContextManager
///
/// @author scx567888
/// @version 0.0.1
public interface ContextManager {

    <T, E extends Throwable> T autoContext(ScxCallable<T, E> handler) throws DataAccessException, E;

    <E extends Throwable> void autoContext(ScxRunnable<E> handler) throws DataAccessException, E;

}
