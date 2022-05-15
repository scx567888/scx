package cool.scx.util.exception;

import cool.scx.ScxHandlerVE;
import cool.scx.ScxHandlerVRE;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutionException;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
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
     * 执行的操作是否有异常 (有异常时不打印信息)
     *
     * @param exceptionScxHandlerVE a
     * @return a
     */
    public static boolean noException(ScxHandlerVE<?> exceptionScxHandlerVE) {
        return noException(exceptionScxHandlerVE, false);
    }

    /**
     * 执行的操作是否有异常 (有异常时根据 printStackTrace 控制是否打印信息)
     *
     * @param exceptionScxHandlerVE a
     * @param printStackTrace       是否打印异常信息
     * @return a
     */
    public static boolean noException(ScxHandlerVE<?> exceptionScxHandlerVE, boolean printStackTrace) {
        try {
            exceptionScxHandlerVE.handle();
            return true;
        } catch (Throwable throwable) {
            if (printStackTrace) {
                throwable.printStackTrace();
            }
            return false;
        }
    }

    /**
     * a
     *
     * @param throwable a
     * @return a
     */
    public static Throwable getRootCause(Throwable throwable) {
        if (throwable instanceof ScxWrappedRuntimeException || throwable instanceof ExecutionException) {
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
