package cool.scx.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>ScxLoggerHelper class.</p>
 *
 * @author scx567888
 * @version 0.0.1
 */
final class ScxLoggerHelper {

    /**
     * 默认格式化时间的类型
     */
    private static final DateTimeFormatter LOG_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * <p>getTimeStamp.</p>
     *
     * @param time a {@link java.time.LocalDateTime} object
     * @return a {@link java.lang.String} object
     */
    public static String getTimeStamp(LocalDateTime time) {
        return LOG_DATETIME_FORMATTER.format(time);
    }

    /**
     * a
     *
     * @param type a
     * @return a
     */
    public static boolean needWriteToConsole(ScxLoggingType type) {
        return type == ScxLoggingType.CONSOLE || type == ScxLoggingType.BOTH;
    }

    /**
     * a
     *
     * @param type a
     * @return a
     */
    public static boolean needWriteToFile(ScxLoggingType type) {
        return type == ScxLoggingType.FILE || type == ScxLoggingType.BOTH;
    }

    /**
     * <p>dontNeedLog.</p>
     *
     * @param level a {@link cool.scx.logging.ScxLoggingLevel} object
     * @return a boolean
     */
    public static boolean dontNeedLog(ScxLoggingLevel level) {
        return level == ScxLoggingLevel.OFF;
    }

    /**
     * 是否为 日志 class 为了减少日志中噪声 我们把日志框架所属的类去除掉
     *
     * @param className className
     * @return a
     */
    public static boolean isLoggerClass(String className) {
        return !className.startsWith("cool.scx.logging") && !className.startsWith("org.slf4j.helpers") && !className.startsWith("org.apache.logging.log4j");
    }

}
