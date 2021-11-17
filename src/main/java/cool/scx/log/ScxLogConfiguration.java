package cool.scx.log;

import com.fasterxml.jackson.core.type.TypeReference;
import cool.scx.ScxAppRoot;
import cool.scx.config.ScxConfig;
import cool.scx.config.handler.impl.AppRootHandler;
import cool.scx.config.handler.impl.ConvertValueHandler;
import cool.scx.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ScxLogConfiguration {

    private static final Map<String, ScxLoggerInfo> loggerInfoMap = new HashMap<>();
    private static ScxLoggerInfo rootScxLoggerInfo = null;

    public static void init(ScxConfig scxConfig, ScxAppRoot scxAppRoot) {
        //先初始化好 rootScxLoggerInfo
        var rootName = "ScxLogRoot";
        var rootLevel = ScxLogLevel.of(scxConfig.getOrDefault("scx.log.root.level", "ERROR"));
        var rootLoggingType = ScxLogLoggingType.of(scxConfig.getOrDefault("scx.log.root.logging-type", "CONSOLE"));
        var rootStoredDirectory = scxConfig.get("scx.log.root.stored-directory", new AppRootHandler("AppRoot:logs", scxAppRoot)).toPath();
        rootScxLoggerInfo = new ScxLoggerInfo(rootName, rootLevel, rootLoggingType, rootStoredDirectory);

        var listMapType = new TypeReference<List<Map<String, String>>>() {

        };
        //以下日志若有缺少的属性则全部以 rootScxLoggerInfo 为准
        var loggers = scxConfig.get("scx.log.loggers", new ConvertValueHandler<>(listMapType));
        if (loggers == null) {
            return;
        }
        for (var logger : loggers) {
            var name = logger.get("name");
            var level = ScxLogLevel.of(logger.get("level"), rootScxLoggerInfo.level());
            var loggingType = ScxLogLoggingType.of(logger.get("logging-type"), rootScxLoggerInfo.loggingType());
            var storedDirectory = StringUtils.isNotBlank(logger.get("stored-directory")) ?
                    scxAppRoot.getFileByAppRoot(logger.get("stored-directory")).toPath()
                    : rootScxLoggerInfo.storedDirectory();

            if (StringUtils.isNotBlank(name)) {
                loggerInfoMap.put(name, new ScxLoggerInfo(name, level, loggingType, storedDirectory));
            }

        }
    }

    public static ScxLoggerInfo getLoggerInfo(String name) {
        return loggerInfoMap.getOrDefault(name, rootScxLoggerInfo);
    }

}
