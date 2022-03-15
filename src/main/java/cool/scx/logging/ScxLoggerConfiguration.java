package cool.scx.logging;

import com.fasterxml.jackson.core.type.TypeReference;
import cool.scx.ScxAppRoot;
import cool.scx.config.ScxConfig;
import cool.scx.config.handler.impl.AppRootHandler;
import cool.scx.config.handler.impl.ConvertValueHandler;
import cool.scx.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * ScxLog 配置类
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxLoggerConfiguration {

    /**
     * a
     */
    public static final DateTimeFormatter LOG_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * a
     *
     * @param scxConfig  a
     * @param scxAppRoot a
     */
    public static void init(ScxConfig scxConfig, ScxAppRoot scxAppRoot) {
        //先初始化好 DefaultScxLoggerInfo
        var defaultLevel = ScxLoggingLevel.of(scxConfig.getOrDefault("scx.logging.default.level", "ERROR"));
        var defaultType = ScxLoggingType.of(scxConfig.getOrDefault("scx.logging.default.type", "CONSOLE"));
        var defaultStoredDirectory = scxConfig.get("scx.logging.default.stored-directory", new AppRootHandler("AppRoot:logs", scxAppRoot)).toPath();
        ScxLoggerFactory.updateDefaultLevel(defaultLevel);
        ScxLoggerFactory.updateDefaultType(defaultType);
        ScxLoggerFactory.updateDefaultStoredDirectory(defaultStoredDirectory);

        //以下日志若有缺少的属性则全部以 defaultScxLoggerInfo 为准
        var loggers = scxConfig.get("scx.logging.loggers", new ConvertValueHandler<>(new TypeReference<List<Map<String, String>>>() {
        }));
        if (loggers == null) {
            return;
        }
        for (var logger : loggers) {
            var name = logger.get("name");
            var level = ScxLoggingLevel.of(logger.get("level"), defaultLevel);
            var type = ScxLoggingType.of(logger.get("type"), defaultType);
            var storedDirectory = StringUtils.isNotBlank(logger.get("stored-directory")) ? scxAppRoot.getFileByAppRoot(logger.get("stored-directory")).toPath() : defaultStoredDirectory;
            if (StringUtils.isNotBlank(name)) {
                ScxLoggerFactory.updateLogger(name, level, type, storedDirectory);
            }
        }
    }

}
