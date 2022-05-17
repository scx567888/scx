package cool.scx.logging;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>ScxLoggerFactory class.</p>
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxLoggerFactory {

    /**
     * Constant <code>LOGGER_CACHE</code>
     */
    private static final Map<String, ScxLogger> LOGGER_CACHE = new HashMap<>();

    /**
     * 日志级别
     */
    private static ScxLoggingLevel defaultLevel = null;

    /**
     * 日志类型
     */
    private static ScxLoggingType defaultType = null;

    /**
     * 存储目录
     */
    private static Path defaultStoredDirectory = null;

    /**
     * 是否启用堆栈跟踪
     */
    private static Boolean defaultStackTrace = null;

    /**
     * 获取默认级别
     *
     * @return a {@link cool.scx.logging.ScxLoggingLevel} object
     */
    static ScxLoggingLevel defaultLevel() {
        return defaultLevel;
    }

    /**
     * 获取默认类型
     *
     * @return a {@link cool.scx.logging.ScxLoggingType} object
     */
    static ScxLoggingType defaultType() {
        return defaultType;
    }

    /**
     * 获取默认存储目录
     *
     * @return a {@link java.nio.file.Path} object
     */
    static Path defaultStoredDirectory() {
        return defaultStoredDirectory;
    }

    /**
     * 获取默认是否启用堆栈跟踪
     *
     * @return a
     */
    static Boolean defaultStackTrace() {
        return defaultStackTrace;
    }

    /**
     * a
     *
     * @param clazz a
     * @return a
     */
    public static ScxLogger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    /**
     * a
     *
     * @param name a
     * @return a
     */
    public static ScxLogger getLogger(String name) {
        var logger = LOGGER_CACHE.get(name);
        if (logger == null) {
            var scxLogger = new ScxLogger(name);
            LOGGER_CACHE.put(name, scxLogger);
            logger = scxLogger;
        }
        return logger;
    }

    /**
     * 更新  Logger 的属性
     *
     * @param name            a {@link java.lang.String} object
     * @param level           a {@link cool.scx.logging.ScxLoggingLevel} object
     * @param type            a {@link cool.scx.logging.ScxLoggingType} object
     * @param storedDirectory a {@link java.nio.file.Path} object
     * @param stackTrace      a {@link java.lang.Boolean} object
     */
    static void updateLoggerInfo(String name, ScxLoggingLevel level, ScxLoggingType type, Path storedDirectory, Boolean stackTrace) {
        getLogger(name).update(level, type, storedDirectory, stackTrace);
    }

    /**
     * <p>updateDefaultLevel.</p>
     *
     * @param newDefaultLevel           a {@link cool.scx.logging.ScxLoggingLevel} object
     * @param newDefaultType            a {@link cool.scx.logging.ScxLoggingType} object
     * @param newDefaultStoredDirectory a {@link java.nio.file.Path} object
     * @param newDefaultStackTrace      a {@link java.lang.Boolean} object
     */
    static void updateDefaultInfo(ScxLoggingLevel newDefaultLevel, ScxLoggingType newDefaultType, Path newDefaultStoredDirectory, Boolean newDefaultStackTrace) {
        defaultLevel = newDefaultLevel;
        defaultType = newDefaultType;
        defaultStoredDirectory = newDefaultStoredDirectory;
        defaultStackTrace = newDefaultStackTrace;
    }

}
