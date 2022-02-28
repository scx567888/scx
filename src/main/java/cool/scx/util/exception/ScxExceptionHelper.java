package cool.scx.util.exception;

import cool.scx.ScxHandlerVE;
import cool.scx.ScxHandlerVRE;

import java.io.PrintWriter;
import java.io.StringWriter;

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
    public static <T> T wrap(ScxHandlerVRE<T, ?> exceptionScxHandlerVRE) {
        try {
            return exceptionScxHandlerVRE.handle();
        } catch (Throwable throwable) {
            throw new ScxWrappedRuntimeException(throwable);
        }
    }

    /**
     * a
     *
     * @param exceptionScxHandlerVE a
     */
    public static void wrap(ScxHandlerVE<?> exceptionScxHandlerVE) {
        try {
            exceptionScxHandlerVE.handle();
        } catch (Throwable throwable) {
            throw new ScxWrappedRuntimeException(throwable);
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

    /**
     * 获取 jdk 内部默认实现的堆栈跟踪字符串
     *
     * @param throwable t
     * @return t
     */
    public static String getStackTraceString(Throwable throwable) {
        var stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

}
