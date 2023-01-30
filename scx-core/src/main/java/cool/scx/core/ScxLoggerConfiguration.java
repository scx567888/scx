package cool.scx.core;

import com.fasterxml.jackson.core.type.TypeReference;
import cool.scx.config.ScxConfig;
import cool.scx.config.ScxEnvironment;
import cool.scx.config.handler_impl.AppRootHandler;
import cool.scx.config.handler_impl.ConvertValueHandler;
import cool.scx.config.handler_impl.DefaultValueHandler;
import cool.scx.logging.ScxLoggerFactory;
import cool.scx.logging.ScxLoggingLevel;
import cool.scx.logging.recorder.ConsoleRecorder;
import cool.scx.logging.recorder.FileRecorder;
import cool.scx.util.ObjectUtils;
import cool.scx.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cool.scx.logging.ScxLoggingLevel.*;
import static java.util.Objects.requireNonNull;

/**
 * ScxLog 配置类
 *
 * @author scx567888
 * @version 1.11.8
 */
final class ScxLoggerConfiguration {

    /**
     * a
     *
     * @param scxConfig      a
     * @param scxEnvironment a
     */
    public static void init(ScxConfig scxConfig, ScxEnvironment scxEnvironment) {
        //先初始化好 DefaultScxLoggerInfo
        var defaultLevel = toLevel(scxConfig.get("scx.logging.default.level", String.class));
        var defaultType = toType(scxConfig.get("scx.logging.default.type", String.class));
        var defaultStoredDirectory = scxConfig.get("scx.logging.default.stored-directory", AppRootHandler.of(scxEnvironment, "AppRoot:logs"));
        var defaultStackTrace = scxConfig.get("scx.logging.default.stack-trace", DefaultValueHandler.of(false));

        //设置默认的 config 这里我们先清除所有的 Recorders
        var defaultConfig = ScxLoggerFactory.defaultConfig().clearRecorders();
        defaultConfig.setLevel(defaultLevel);
        if (defaultType == LoggingType.CONSOLE || defaultType == LoggingType.BOTH) {
            defaultConfig.addRecorder(new ConsoleRecorder());
        }
        if (defaultType == LoggingType.FILE || defaultType == LoggingType.BOTH) {
            defaultConfig.addRecorder(new FileRecorder(defaultStoredDirectory));
        }
        defaultConfig.setStackTrace(defaultStackTrace);

        //以下日志若有缺少的 storedDirectory 则全部以 defaultStoredDirectory 为准
        var loggers = scxConfig.get("scx.logging.loggers", ConvertValueHandler.of(new TypeReference<List<Map<String, String>>>() {
        }));
        if (loggers != null) {
            for (var logger : loggers) {
                var name = logger.get("name");
                if (StringUtils.notBlank(name)) {
                    var level = toLevel(logger.get("level"));
                    var type = toType(logger.get("type"));
                    var storedDirectory = StringUtils.notBlank(logger.get("stored-directory")) ? scxEnvironment.getPathByAppRoot(logger.get("stored-directory")) : null;
                    var stackTrace = ObjectUtils.convertValue(logger.get("stack-trace"), Boolean.class);
                    var config = ScxLoggerFactory.getLogger(name).config();
                    config.setLevel(level);
                    if (type == LoggingType.CONSOLE || type == LoggingType.BOTH) {
                        config.addRecorder(new ConsoleRecorder());
                    }
                    if (type == LoggingType.FILE || type == LoggingType.BOTH) {
                        //文件路径的缺省值使用 默认的
                        config.addRecorder(new FileRecorder(storedDirectory != null ? storedDirectory : defaultStoredDirectory));
                    }
                    config.setStackTrace(stackTrace);
                }
            }
        }
    }

    /**
     * <p>toLevel.</p>
     *
     * @param levelName a {@link java.lang.String} object
     * @return a {@link cool.scx.logging.ScxLoggingLevel} object
     */
    private static ScxLoggingLevel toLevel(String levelName) {
        Objects.requireNonNull(levelName, "levelName 不能为空 !!!");
        var s = levelName.trim().toUpperCase();
        return switch (s) {
            case "OFF", "O" -> OFF;
            case "FATAL", "F" -> FATAL;
            case "ERROR", "E" -> ERROR;
            case "WARN", "W" -> WARN;
            case "INFO", "I" -> INFO;
            case "DEBUG", "D" -> DEBUG;
            case "TRACE", "T" -> TRACE;
            case "ALL", "A" -> ALL;
            default -> null;
        };
    }

    /**
     * <p>toType.</p>
     *
     * @param loggingTypeName a {@link java.lang.String} object
     * @return a {@link cool.scx.core.ScxLoggerConfiguration.LoggingType} object
     */
    private static LoggingType toType(String loggingTypeName) {
        requireNonNull(loggingTypeName, "loggingTypeName 不能为空 !!!");
        var s = loggingTypeName.trim().toUpperCase();
        return switch (s) {
            case "CONSOLE", "C" -> LoggingType.CONSOLE;
            case "FILE", "F" -> LoggingType.FILE;
            case "BOTH", "B" -> LoggingType.BOTH;
            default -> null;
        };
    }

    private enum LoggingType {

        /**
         * 打印到控制台
         */
        CONSOLE,

        /**
         * 写入到文件
         */
        FILE,

        /**
         * 既打印到控制台也同时写入到文件
         */
        BOTH
    }

}
