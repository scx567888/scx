package cool.scx.util.exception;

import cool.scx.ScxHandlerVE;
import cool.scx.ScxHandlerVRE;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.security.CodeSource;

/**
 * a
 */
public final class ScxExceptionHelper {

    /**
     * 默认 classLoader
     */
    private static final ClassLoader DEFAULT_CLASS_LOADER = ClassLoader.getSystemClassLoader();

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

    /**
     * 获取 jdk 内部默认实现的堆栈跟踪字符串
     *
     * @param throwable t
     * @return t
     */
    public static String getNormalStackTrace(Throwable throwable) {
        var stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 获取自定义的 堆栈跟踪信息
     *
     * @param throwable t
     * @return t
     */
    public static String getCustomStackTrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        var stringBuilder = new StringBuilder();
        stringBuilder.append(throwable).append(System.lineSeparator());
        var stackTrace = throwable.getStackTrace();
        for (var st : stackTrace) {
            var s = st.getClassName() + "." + st.getMethodName() + "(" +
                    (st.isNativeMethod() ? "Native Method)" :
                            (st.getFileName() != null && st.getLineNumber() >= 0 ?
                                    st.getFileName() + ":" + st.getLineNumber() + ")" :
                                    (st.getFileName() != null ? "" + st.getFileName() + ")" : "Unknown Source)")));
            Class<?> c = null;
            try {
                c = DEFAULT_CLASS_LOADER.loadClass(st.getClassName());
            } catch (ClassNotFoundException ignored) {

            }
            stringBuilder.append("\tat ").append(s).append(" ").append(getJarAndVersion(c)).append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    private static String getJarAndVersion(final Class<?> callerClass) {
        String location = "?";
        String version = "?";
        if (callerClass != null) {
            try {
                final CodeSource source = callerClass.getProtectionDomain().getCodeSource();
                if (source != null) {
                    final URL locationURL = source.getLocation();
                    if (locationURL != null) {
                        final String str = locationURL.toString().replace('\\', '/');
                        int index = str.lastIndexOf("/");
                        if (index >= 0 && index == str.length() - 1) {
                            index = str.lastIndexOf("/", index - 1);
                        }
                        location = str.substring(index + 1);
                    }
                }
            } catch (final Exception ex) {
                // Ignore the exception.
            }
            final Package pkg = callerClass.getPackage();
            if (pkg != null) {
                final String ver = pkg.getImplementationVersion();
                if (ver != null) {
                    version = ver;
                }
            }
        }
        return "~[" + location + ":" + version + "]";
    }

}
