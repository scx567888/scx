package cool.scx.core;

import com.fasterxml.jackson.core.type.TypeReference;
import cool.scx.config.ScxConfig;
import cool.scx.config.ScxEnvironment;
import cool.scx.config.handler_impl.AppRootHandler;
import cool.scx.config.handler_impl.ConvertValueHandler;
import cool.scx.config.handler_impl.DefaultValueHandler;
import cool.scx.logging.ScxLoggerFactory;
import cool.scx.logging.ScxLoggingLevel;
import cool.scx.logging.ScxLoggingType;
import cool.scx.util.ObjectUtils;
import cool.scx.util.StringUtils;

import java.util.List;
import java.util.Map;

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
        initDefault(scxConfig, scxEnvironment);
        initLoggers(scxConfig, scxEnvironment);
    }

    /**
     * <p>initDefault.</p>
     *
     * @param scxConfig      a {@link cool.scx.config.ScxConfig} object
     * @param scxEnvironment a {@link cool.scx.config.ScxEnvironment} object
     */
    private static void initDefault(ScxConfig scxConfig, ScxEnvironment scxEnvironment) {
        //先初始化好 DefaultScxLoggerInfo
        var defaultLevel = ScxLoggingLevel.of(scxConfig.get("scx.logging.default.level", String.class), ScxLoggingLevel.ERROR);
        var defaultType = ScxLoggingType.of(scxConfig.get("scx.logging.default.type", String.class), ScxLoggingType.CONSOLE);
        var defaultStoredDirectory = scxConfig.get("scx.logging.default.stored-directory", AppRootHandler.of(scxEnvironment, "AppRoot:logs"));
        var defaultStackTrace = scxConfig.get("scx.logging.default.stack-trace", DefaultValueHandler.of(false));
        ScxLoggerFactory.setDefaultLevel(defaultLevel);
        ScxLoggerFactory.setDefaultType(defaultType);
        ScxLoggerFactory.setDefaultStoredDirectory(defaultStoredDirectory);
        ScxLoggerFactory.setDefaultStackTrace(defaultStackTrace);
    }

    /**
     * <p>initLoggers.</p>
     *
     * @param scxConfig      a {@link cool.scx.config.ScxConfig} object
     * @param scxEnvironment a {@link cool.scx.config.ScxEnvironment} object
     */
    private static void initLoggers(ScxConfig scxConfig, ScxEnvironment scxEnvironment) {
        //以下日志若有缺少的属性则全部以 defaultScxLoggerInfo 为准
        var loggers = scxConfig.get("scx.logging.loggers", ConvertValueHandler.of(new TypeReference<List<Map<String, String>>>() {
        }));
        if (loggers != null) {
            for (var logger : loggers) {
                var name = logger.get("name");
                if (StringUtils.notBlank(name)) {
                    var level = ScxLoggingLevel.of(logger.get("level"), null);
                    var type = ScxLoggingType.of(logger.get("type"), null);
                    var storedDirectory = StringUtils.notBlank(logger.get("stored-directory")) ? scxEnvironment.getPathByAppRoot(logger.get("stored-directory")) : null;
                    var stackTrace = ObjectUtils.convertValue(logger.get("stack-trace"), Boolean.class);
                    ScxLoggerFactory.getLogger(name).setLevel(level).setType(type).setStoredDirectory(storedDirectory).setStackTrace(stackTrace);
                }
            }
        }
    }

}
