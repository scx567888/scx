package cool.scx.logging;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
     * Constant <code>defaultLevel</code>
     */
    private static ScxLoggingLevel defaultLevel = ScxLoggingLevel.ERROR;
    /**
     * Constant <code>defaultType</code>
     */
    private static ScxLoggingType defaultType = ScxLoggingType.CONSOLE;
    /**
     * Constant <code>defaultStoredDirectory</code>
     */
    private static Path defaultStoredDirectory = null;

    /**
     * <p>defaultLevel.</p>
     *
     * @return a {@link cool.scx.logging.ScxLoggingLevel} object
     */
    static ScxLoggingLevel defaultLevel() {
        return defaultLevel;
    }

    /**
     * <p>defaultType.</p>
     *
     * @return a {@link cool.scx.logging.ScxLoggingType} object
     */
    static ScxLoggingType defaultType() {
        return defaultType;
    }

    /**
     * <p>defaultStoredDirectory.</p>
     *
     * @return a {@link java.nio.file.Path} object
     */
    static Path defaultStoredDirectory() {
        return defaultStoredDirectory;
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
     */
    static void updateLogger(String name, ScxLoggingLevel level, ScxLoggingType type, Path storedDirectory) {
        var logger = getLogger(name);
        if (level != null) {
            logger.level(level);
        }
        if (type != null) {
            logger.type(type);
        }
        if (storedDirectory != null) {
            logger.storedDirectory(storedDirectory);
        }
    }

    /**
     * <p>updateDefaultLevel.</p>
     *
     * @param newDefaultLevel a {@link cool.scx.logging.ScxLoggingLevel} object
     */
    static void updateDefaultLevel(ScxLoggingLevel newDefaultLevel) {
        Objects.requireNonNull(newDefaultLevel);
        defaultLevel = newDefaultLevel;
    }

    /**
     * <p>updateDefaultType.</p>
     *
     * @param newDefaultType a {@link cool.scx.logging.ScxLoggingType} object
     */
    static void updateDefaultType(ScxLoggingType newDefaultType) {
        Objects.requireNonNull(newDefaultType);
        defaultType = newDefaultType;
    }

    /**
     * <p>updateDefaultStoredDirectory.</p>
     *
     * @param newDefaultStoredDirectory a {@link java.nio.file.Path} object
     */
    static void updateDefaultStoredDirectory(Path newDefaultStoredDirectory) {
        Objects.requireNonNull(newDefaultStoredDirectory);
        defaultStoredDirectory = newDefaultStoredDirectory;
    }

}
