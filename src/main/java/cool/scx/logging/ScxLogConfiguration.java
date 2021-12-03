package cool.scx.logging;

import com.fasterxml.jackson.core.type.TypeReference;
import cool.scx.ScxAppRoot;
import cool.scx.config.ScxConfig;
import cool.scx.config.handler.impl.AppRootHandler;
import cool.scx.config.handler.impl.ConvertValueHandler;
import cool.scx.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ScxLog 配置类
 */
public final class ScxLogConfiguration {

    /**
     *
     */
    private static final Map<String, ScxLoggerInfo> loggerInfoMap = new HashMap<>();

    /**
     * root
     */
    private static ScxLoggerInfo defaultScxLoggerInfo = new ScxLoggerInfo("DefaultScxLoggerInfo", ScxLogLevel.ERROR, ScxLogLoggingType.CONSOLE, null);

    /**
     * a
     *
     * @param scxConfig  a
     * @param scxAppRoot a
     */
    public static void init(ScxConfig scxConfig, ScxAppRoot scxAppRoot) {
        //先初始化好 DefaultScxLoggerInfo
        var tempName = "DefaultScxLoggerInfo";
        var tempLevel = ScxLogLevel.of(scxConfig.getOrDefault("scx.logging.default.level", "ERROR"));
        var tempLoggingType = ScxLogLoggingType.of(scxConfig.getOrDefault("scx.logging.default.logging-type", "CONSOLE"));
        var tempStoredDirectory = scxConfig.get("scx.logging.default.stored-directory", new AppRootHandler("AppRoot:logs", scxAppRoot)).toPath();
        defaultScxLoggerInfo = new ScxLoggerInfo(tempName, tempLevel, tempLoggingType, tempStoredDirectory);

        var listMapType = new TypeReference<List<Map<String, String>>>() {

        };
        //以下日志若有缺少的属性则全部以 defaultScxLoggerInfo 为准
        var loggers = scxConfig.get("scx.logging.loggers", new ConvertValueHandler<>(listMapType));
        if (loggers == null) {
            return;
        }
        for (var logger : loggers) {
            var name = logger.get("name");
            var level = ScxLogLevel.of(logger.get("level"), defaultScxLoggerInfo.level());
            var loggingType = ScxLogLoggingType.of(logger.get("logging-type"), defaultScxLoggerInfo.loggingType());
            var storedDirectory = StringUtils.isNotBlank(logger.get("stored-directory")) ? scxAppRoot.getFileByAppRoot(logger.get("stored-directory")).toPath() : defaultScxLoggerInfo.storedDirectory();

            if (StringUtils.isNotBlank(name)) {
                loggerInfoMap.put(name, new ScxLoggerInfo(name, level, loggingType, storedDirectory));
            }

        }
    }

    /**
     * a
     *
     * @param name a
     * @return a
     */
    public static ScxLoggerInfo getLoggerInfo(String name) {
        return loggerInfoMap.getOrDefault(name, defaultScxLoggerInfo);
    }

}
