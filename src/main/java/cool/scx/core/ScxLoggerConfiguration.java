package cool.scx.core;

import com.fasterxml.jackson.core.type.TypeReference;
import cool.scx.config.ScxConfig;
import cool.scx.config.handler.AppRootHandler;
import cool.scx.config.handler.ConvertValueHandler;
import cool.scx.config.handler.DefaultValueHandler;
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
     * @param scxEnvironment a {@link ScxEnvironment} object
     */
    private static void initDefault(ScxConfig scxConfig, ScxEnvironment scxEnvironment) {
        //先初始化好 DefaultScxLoggerInfo
        var defaultLevel = ScxLoggingLevel.of(scxConfig.get("scx.logging.default.level", String.class), ScxLoggingLevel.ERROR);
        var defaultType = ScxLoggingType.of(scxConfig.get("scx.logging.default.type", String.class), ScxLoggingType.CONSOLE);
        var defaultStoredDirectory = scxConfig.get("scx.logging.default.stored-directory", new AppRootHandler("AppRoot:logs", scxEnvironment));
        var defaultStackTrace = scxConfig.get("scx.logging.default.stack-trace", new DefaultValueHandler<>(false));
        ScxLoggerFactory.updateDefault(defaultLevel, defaultType, defaultStoredDirectory, defaultStackTrace);
    }

    /**
     * <p>initLoggers.</p>
     *
     * @param scxConfig      a {@link cool.scx.config.ScxConfig} object
     * @param scxEnvironment a {@link ScxEnvironment} object
     */
    private static void initLoggers(ScxConfig scxConfig, ScxEnvironment scxEnvironment) {
        //以下日志若有缺少的属性则全部以 defaultScxLoggerInfo 为准
        var loggers = scxConfig.get("scx.logging.loggers", new ConvertValueHandler<>(new TypeReference<List<Map<String, String>>>() {
        }));
        if (loggers != null) {
            for (var logger : loggers) {
                var name = logger.get("name");
                if (StringUtils.notBlank(name)) {
                    var level = ScxLoggingLevel.of(logger.get("level"), null);
                    var type = ScxLoggingType.of(logger.get("type"), null);
                    var storedDirectory = StringUtils.notBlank(logger.get("stored-directory")) ? scxEnvironment.getPathByAppRoot(logger.get("stored-directory")) : null;
                    var stackTrace = ObjectUtils.convertValue(logger.get("stack-trace"), Boolean.class);
                    ScxLoggerFactory.updateLogger(name, level, type, storedDirectory, stackTrace);
                }
            }
        }
    }

}
