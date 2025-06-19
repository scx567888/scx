package cool.scx.data;

import cool.scx.data.exception.DataAccessException;
import cool.scx.functional.ScxCallable;
import cool.scx.functional.ScxConsumer;
import cool.scx.functional.ScxFunction;
import cool.scx.functional.ScxRunnable;

public interface DataManager<C extends DataContext> {

    C createDataContext();

    default <T, E extends Throwable> T withContext(ScxFunction<C, T, E> handler) throws DataAccessException, E {
        C dataContext = createDataContext();
        return handler.apply(dataContext);
    }

    default <E extends Throwable> void withContext(ScxConsumer<C, E> handler) throws DataAccessException, E {
        C dataContext = createDataContext();
        handler.accept(dataContext);
    }

    default <T, E extends Throwable> T autoContext(ScxCallable<T, E> handler) throws DataAccessException, E {
        return handler.call();
    }

    default <E extends Throwable> void autoContext(ScxRunnable<E> handler) throws DataAccessException, E {
        handler.run();
    }

}
