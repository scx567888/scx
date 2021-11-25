package cool.scx.exception;

import cool.scx.ScxHandlerVE;
import cool.scx.ScxHandlerVRE;

public final class ScxExceptionHelper {

    public static <T> T wrap(ScxHandlerVRE<T, Exception> exceptionScxHandlerVRE) {
        try {
            return exceptionScxHandlerVRE.handle();
        } catch (Exception exception) {
            throw new ScxWrappedRuntimeException(exception);
        }
    }

    public static void wrap(ScxHandlerVE<Exception> exceptionScxHandlerVE) {
        try {
            exceptionScxHandlerVE.handle();
        } catch (Exception exception) {
            throw new ScxWrappedRuntimeException(exception);
        }
    }

    public static Throwable getRootCause(Throwable throwable) {
        if (throwable instanceof ScxWrappedRuntimeException) {
            return getRootCause(throwable.getCause());
        }
        return throwable;
    }

}
