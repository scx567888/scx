package cool.scx.logging;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static cool.scx.logging.ScxLoggerMessageFormatter.DEFAULT_SCX_LOGGER_MESSAGE_FORMATTER;
import static java.util.Objects.requireNonNull;

/**
 * ScxLoggerFactory
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxLoggerFactory {

    /**
     * 存储所有的 日志对象
     */
    private static final Map<String, ScxLogger> LOGGER_MAP = new HashMap<>();

    /**
     * 日志类提供者 可以通过重写来达到拓展的效果
     */
    private static Function<String, ScxLogger> loggerSupplier = ScxLogger::new;

    /**
     * 默认的日志级别
     */
    private static ScxLoggingLevel defaultLevel = ScxLoggingLevel.ERROR;

    /**
     * 默认的日志类型
     */
    private static ScxLoggingType defaultType = ScxLoggingType.CONSOLE;

    /**
     * 默认的存储目录
     */
    private static Path defaultStoredDirectory = null;

    /**
     * 默认是否启用堆栈跟踪
     */
    private static boolean defaultStackTrace = false;

    /**
     * 默认 message 格式化器
     */
    private static ScxLoggerMessageFormatter defaultMessageFormatter = DEFAULT_SCX_LOGGER_MESSAGE_FORMATTER;

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
    static boolean defaultStackTrace() {
        return defaultStackTrace;
    }

    /**
     * 获取默认的消息格式化器
     *
     * @return a
     */
    static ScxLoggerMessageFormatter defaultMessageFormatter() {
        return defaultMessageFormatter;
    }

    /**
     * 设置 logger 提供者
     *
     * @param newLoggerSupplier Function 返回值不允许为空
     */
    public static void setLoggerSupplier(Function<String, ScxLogger> newLoggerSupplier) {
        requireNonNull(newLoggerSupplier, "loggerSupplier 不能为 null");
        loggerSupplier = newLoggerSupplier;
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
        return LOGGER_MAP.computeIfAbsent(name, loggerSupplier);
    }

    /**
     * <p>Setter for the field <code>defaultLevel</code>.</p>
     *
     * @param newLevel a {@link cool.scx.logging.ScxLoggingLevel} object
     */
    public static void setDefaultLevel(ScxLoggingLevel newLevel) {
        requireNonNull(newLevel, "defaultLevel 不能为 null");
        defaultLevel = newLevel;
    }

    /**
     * <p>Setter for the field <code>defaultType</code>.</p>
     *
     * @param newType a {@link cool.scx.logging.ScxLoggingType} object
     */
    public static void setDefaultType(ScxLoggingType newType) {
        requireNonNull(newType, "defaultType 不能为 null");
        defaultType = newType;
    }

    /**
     * <p>Setter for the field <code>defaultStoredDirectory</code>.</p>
     *
     * @param newStoredDirectory a {@link java.nio.file.Path} object
     */
    public static void setDefaultStoredDirectory(Path newStoredDirectory) {
        defaultStoredDirectory = newStoredDirectory;
    }

    /**
     * <p>Setter for the field <code>defaultStackTrace</code>.</p>
     *
     * @param newStackTrace a boolean
     */
    public static void setDefaultStackTrace(boolean newStackTrace) {
        defaultStackTrace = newStackTrace;
    }

    /**
     * 设置默认的 message 格式化器
     *
     * @param newMessageFormatter a
     */
    public static void setDefaultMessageFormatter(ScxLoggerMessageFormatter newMessageFormatter) {
        requireNonNull(newMessageFormatter, "defaultMessageFormatter 不能为 null");
        defaultMessageFormatter = newMessageFormatter;
    }

}
