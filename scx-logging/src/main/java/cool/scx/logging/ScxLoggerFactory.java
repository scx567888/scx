package cool.scx.logging;

import cool.scx.logging.recorder.ConsoleRecorder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * ScxLoggerFactory
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxLoggerFactory {

    /**
     * Constant <code>LOGGER_MAP</code>
     */
    private static final Map<String, ScxLogger> LOGGER_MAP = new ConcurrentHashMap<>();

    /**
     * Constant <code>DEFAULT_CONFIG</code>
     */
    private static final ScxLoggerConfig DEFAULT_CONFIG = new ScxLoggerConfig(null).addRecorder(new ConsoleRecorder());

    /**
     * Constant <code>loggerSupplier</code>
     */
    private static Function<String, ScxLogger> loggerSupplier = ScxLogger::new;

    /**
     * <p>getLogger.</p>
     *
     * @param name a {@link java.lang.String} object
     * @return a {@link cool.scx.logging.ScxLogger} object
     */
    public static ScxLogger getLogger(String name) {
        return LOGGER_MAP.computeIfAbsent(name, loggerSupplier);
    }

    /**
     * <p>getLogger.</p>
     *
     * @param clazz a {@link java.lang.Class} object
     * @return a {@link cool.scx.logging.ScxLogger} object
     */
    public static ScxLogger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
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
     * <p>defaultConfig.</p>
     *
     * @return a {@link cool.scx.logging.ScxLoggerConfig} object
     */
    public static ScxLoggerConfig defaultConfig() {
        return DEFAULT_CONFIG;
    }

}
