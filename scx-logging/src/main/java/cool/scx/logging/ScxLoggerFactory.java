package cool.scx.logging;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        return LOGGER_MAP.computeIfAbsent(name, ScxLogger::new);
    }

    /**
     * <p>Setter for the field <code>defaultLevel</code>.</p>
     *
     * @param newLevel a {@link cool.scx.logging.ScxLoggingLevel} object
     */
    public static void setDefaultLevel(ScxLoggingLevel newLevel) {
        Objects.requireNonNull(newLevel, "defaultLevel 不能为 null");
        defaultLevel = newLevel;
    }

    /**
     * <p>Setter for the field <code>defaultType</code>.</p>
     *
     * @param newType a {@link cool.scx.logging.ScxLoggingType} object
     */
    public static void setDefaultType(ScxLoggingType newType) {
        Objects.requireNonNull(newType, "defaultType 不能为 null");
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
     * <p>setLevel.</p>
     *
     * @param name     a {@link java.lang.String} object
     * @param newLevel a {@link cool.scx.logging.ScxLoggingLevel} object
     */
    public static void setLevel(String name, ScxLoggingLevel newLevel) {
        getLogger(name).setLevel(newLevel);
    }

    /**
     * <p>setType.</p>
     *
     * @param name    a {@link java.lang.String} object
     * @param newType a {@link cool.scx.logging.ScxLoggingType} object
     */
    public static void setType(String name, ScxLoggingType newType) {
        getLogger(name).setType(newType);
    }

    /**
     * <p>setStoredDirectory.</p>
     *
     * @param name               a {@link java.lang.String} object
     * @param newStoredDirectory a {@link java.nio.file.Path} object
     */
    public static void setStoredDirectory(String name, Path newStoredDirectory) {
        getLogger(name).setStoredDirectory(newStoredDirectory);
    }

    /**
     * <p>setStackTrace.</p>
     *
     * @param name          a {@link java.lang.String} object
     * @param newStackTrace a boolean
     */
    public static void setStackTrace(String name, boolean newStackTrace) {
        getLogger(name).setStackTrace(newStackTrace);
    }

    /**
     * <p>setLevel.</p>
     *
     * @param clazz    a {@link java.lang.Class} object
     * @param newLevel a {@link cool.scx.logging.ScxLoggingLevel} object
     */
    public static void setLevel(Class<?> clazz, ScxLoggingLevel newLevel) {
        getLogger(clazz).setLevel(newLevel);
    }

    /**
     * <p>setType.</p>
     *
     * @param clazz   a {@link java.lang.Class} object
     * @param newType a {@link cool.scx.logging.ScxLoggingType} object
     */
    public static void setType(Class<?> clazz, ScxLoggingType newType) {
        getLogger(clazz).setType(newType);
    }

    /**
     * <p>setStoredDirectory.</p>
     *
     * @param clazz              a {@link java.lang.Class} object
     * @param newStoredDirectory a {@link java.nio.file.Path} object
     */
    public static void setStoredDirectory(Class<?> clazz, Path newStoredDirectory) {
        getLogger(clazz).setStoredDirectory(newStoredDirectory);
    }

    /**
     * <p>setStackTrace.</p>
     *
     * @param clazz         a {@link java.lang.Class} object
     * @param newStackTrace a boolean
     */
    public static void setStackTrace(Class<?> clazz, boolean newStackTrace) {
        getLogger(clazz).setStackTrace(newStackTrace);
    }


    /**
     * 更新指定的 Logger 信息
     *
     * @param name            a {@link java.lang.String} object
     * @param level           a {@link cool.scx.logging.ScxLoggingLevel} object
     * @param type            a {@link cool.scx.logging.ScxLoggingType} object
     * @param storedDirectory a {@link java.nio.file.Path} object
     * @param stackTrace      a {@link java.lang.Boolean} object
     */
    public static void updateLogger(String name, ScxLoggingLevel level, ScxLoggingType type, Path storedDirectory, Boolean stackTrace) {
        getLogger(name).setLevel(level).setType(type).setStoredDirectory(storedDirectory).setStackTrace(stackTrace);
    }

    /**
     * 更新指定的 Logger 信息
     *
     * @param clazz           a
     * @param level           a
     * @param type            a
     * @param storedDirectory a
     * @param stackTrace      a
     */
    public static void updateLogger(Class<?> clazz, ScxLoggingLevel level, ScxLoggingType type, Path storedDirectory, Boolean stackTrace) {
        getLogger(clazz).setLevel(level).setType(type).setStoredDirectory(storedDirectory).setStackTrace(stackTrace);
    }

    /**
     * 更新默认日志信息
     *
     * @param newDefaultLevel           a {@link cool.scx.logging.ScxLoggingLevel} object
     * @param newDefaultType            a {@link cool.scx.logging.ScxLoggingType} object
     * @param newDefaultStoredDirectory a {@link java.nio.file.Path} object
     * @param newDefaultStackTrace      a {@link java.lang.Boolean} object
     */
    public static void updateDefault(ScxLoggingLevel newDefaultLevel, ScxLoggingType newDefaultType, Path newDefaultStoredDirectory, boolean newDefaultStackTrace) {
        setDefaultLevel(newDefaultLevel);
        setDefaultType(newDefaultType);
        setDefaultStoredDirectory(newDefaultStoredDirectory);
        setDefaultStackTrace(newDefaultStackTrace);
    }

}
