package cool.scx.exception;

import cool.scx.ScxHandlerVE;
import cool.scx.ScxHandlerVRE;

/**
 * a
 */
public final class ScxExceptionHelper {

    /**
     * a
     *
     * @param exceptionScxHandlerVRE a
     * @param <T>                    a
     * @return a
     */
    public static <T> T wrap(ScxHandlerVRE<T, Exception> exceptionScxHandlerVRE) {
        try {
            return exceptionScxHandlerVRE.handle();
        } catch (Exception exception) {
            throw new ScxWrappedRuntimeException(exception);
        }
    }

    /**
     * a
     *
     * @param exceptionScxHandlerVE a
     */
    public static void wrap(ScxHandlerVE<Exception> exceptionScxHandlerVE) {
        try {
            exceptionScxHandlerVE.handle();
        } catch (Exception exception) {
            throw new ScxWrappedRuntimeException(exception);
        }
    }

    /**
     * a
     *
     * @param throwable a
     * @return a
     */
    public static Throwable getRootCause(Throwable throwable) {
        if (throwable instanceof ScxWrappedRuntimeException) {
            return getRootCause(throwable.getCause());
        }
        return throwable;
    }

}
