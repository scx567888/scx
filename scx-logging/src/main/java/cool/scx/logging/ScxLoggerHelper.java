package cool.scx.logging;

import java.util.ArrayList;

/**
 * <p>ScxLoggerHelper class.</p>
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxLoggerHelper {

    /**
     * 是否为 日志 class 为了减少日志中噪声 我们把日志框架所属的类去除掉
     *
     * @param className className
     * @return a
     */
    public static boolean isLoggerClass(String className) {
        return !className.startsWith("cool.scx.logging") && !className.startsWith("org.slf4j.helpers") && !className.startsWith("org.apache.logging.log4j");
    }

    /**
     * 获取过滤后的堆栈信息 (字符串形式)
     *
     * @param stackTraces a
     * @return a {@link java.lang.String} object
     */
    public static String formatStackTrace(StackTraceElement[] stackTraces) {
        var sb = new StringBuilder();
        for (var traceElement : stackTraces) {
            sb.append("\t").append(traceElement).append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * 过滤掉 日志框架的堆栈信息
     *
     * @param e a
     * @return a
     */
    public static StackTraceElement[] getFilteredStackTrace(Exception e) {
        var list = new ArrayList<StackTraceElement>();
        for (var traceElement : e.getStackTrace()) {
            if (isLoggerClass(traceElement.getClassName())) {
                list.add(traceElement);
            }
        }
        return list.toArray(StackTraceElement[]::new);
    }

}
