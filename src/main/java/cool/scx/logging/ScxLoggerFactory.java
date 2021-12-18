package cool.scx.logging;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
public final class ScxLoggerFactory {

    private static final Map<String, ScxLogger> LOGGER_CACHE = new HashMap<>();

    private static ScxLoggingLevel defaultLevel = ScxLoggingLevel.ERROR;
    private static ScxLoggingType defaultType = ScxLoggingType.CONSOLE;
    private static Path defaultStoredDirectory = null;

    static ScxLoggingLevel defaultLevel() {
        return defaultLevel;
    }

    static ScxLoggingType defaultType() {
        return defaultType;
    }

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

    static void updateDefaultLevel(ScxLoggingLevel newDefaultLevel) {
        Objects.requireNonNull(newDefaultLevel);
        defaultLevel = newDefaultLevel;
    }

    static void updateDefaultType(ScxLoggingType newDefaultType) {
        Objects.requireNonNull(newDefaultType);
        defaultType = newDefaultType;
    }

    static void updateDefaultStoredDirectory(Path newDefaultStoredDirectory) {
        Objects.requireNonNull(newDefaultStoredDirectory);
        defaultStoredDirectory = newDefaultStoredDirectory;
    }

}
