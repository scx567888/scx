package cool.scx.common.util;

import cool.scx.common.functional.ScxRunnable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

/**
 * ScxExceptionHelper
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxExceptionUtils {

    /**
     * 包装 异常
     *
     * @param handler a
     * @param <T>     a
     * @return a
     */
    public static <T> T wrap(Callable<T> handler) {
        try {
            return handler.call();
        } catch (Throwable throwable) {
            throw new ScxWrappedRuntimeException(throwable);
        }
    }

    /**
     * 包装 异常
     *
     * @param handler a
     */
    public static void wrap(ScxRunnable<?> handler) {
        try {
            handler.run();
        } catch (Throwable throwable) {
            throw new ScxWrappedRuntimeException(throwable);
        }
    }

    /**
     * 忽略异常 发生异常时返回 null
     *
     * @param handler a
     * @param <T>     a
     * @return a
     */
    public static <T> T ignore(Callable<T> handler) {
        try {
            return handler.call();
        } catch (Throwable throwable) {
            return null;
        }
    }

    /**
     * 忽略异常 发生异常时返回默认值
     *
     * @param handler    a
     * @param <T>        a
     * @param defaultVal a T object
     * @return a
     */
    public static <T> T ignore(Callable<T> handler, T defaultVal) {
        try {
            return handler.call();
        } catch (Throwable throwable) {
            return defaultVal;
        }
    }

    /**
     * 忽略异常
     *
     * @param handler a
     */
    public static void ignore(ScxRunnable<?> handler) {
        try {
            handler.run();
        } catch (Throwable ignored) {

        }
    }

    /**
     * 执行的操作是否有异常 (有异常时不打印信息)
     *
     * @param scxRunnable a
     * @return a
     */
    public static boolean noException(ScxRunnable<?> scxRunnable) {
        try {
            scxRunnable.run();
            return true;
        } catch (Throwable throwable) {
            return false;
        }
    }

    /**
     * 解包包装后的异常
     *
     * @param throwable a
     * @return a
     */
    public static Throwable getRootCause(Throwable throwable) {
        if (throwable instanceof ScxWrappedRuntimeException ||
            throwable instanceof ExecutionException ||
            throwable instanceof CompletionException) {
            return getRootCause(throwable.getCause());
        } else {
            return throwable;
        }
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
        return stringWriter.getBuffer().toString();
    }

    /**
     * ScxWrappedRuntimeException  Scx 自定义异常包装器
     *
     * @author scx567888
     * @version 0.0.1
     */
    public static final class ScxWrappedRuntimeException extends RuntimeException {

        public ScxWrappedRuntimeException(Throwable cause) {
            super(cause);
        }

    }

}
