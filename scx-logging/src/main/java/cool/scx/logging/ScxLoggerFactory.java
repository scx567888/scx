package cool.scx.logging;

import cool.scx.logging.recorder.ConsoleRecorder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * ScxLoggerFactory
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxLoggerFactory {

    private static final Map<String, ScxLogger> LOGGER_MAP = new HashMap<>();

    private static final ScxLoggerConfig DEFAULT_CONFIG = new ScxLoggerConfig(null).addRecorder(new ConsoleRecorder());

    private static Function<String, ScxLogger> loggerSupplier = ScxLogger::new;

    public static ScxLogger getLogger(String name) {
        return LOGGER_MAP.computeIfAbsent(name, loggerSupplier);
    }

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

    public static ScxLoggerConfig defaultConfig() {
        return DEFAULT_CONFIG;
    }

}
