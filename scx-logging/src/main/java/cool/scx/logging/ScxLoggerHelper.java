package cool.scx.logging;

import java.util.ArrayList;

/**
 * <p>ScxLoggerHelper class.</p>
 *
 * @author scx567888
 * @version 0.0.1
 */
final class ScxLoggerHelper {

    /**
     * 是否为 日志 class 为了减少日志中噪声 我们把日志框架所属的类去除掉
     *
     * @param className className
     * @return a
     */
    public static boolean isLoggerClass(String className) {
        return !className.startsWith("cool.scx.logging")
               && !className.startsWith("org.slf4j.helpers")
               && !className.startsWith("org.apache.logging.log4j")
               && !className.startsWith("java.lang.System$Logger");
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
