package cool.scx.logging;

import java.util.ArrayList;

/// ScxLoggerHelper
///
/// @author scx567888
/// @version 0.0.1
final class ScxLoggerHelper {

    public static boolean isLoggerClass(String className) {
        return !className.startsWith("cool.scx.logging") &&
                !className.startsWith("org.slf4j.helpers") &&
                !className.startsWith("org.apache.logging.log4j") &&
                !className.startsWith("java.lang.System$Logger");
    }

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
