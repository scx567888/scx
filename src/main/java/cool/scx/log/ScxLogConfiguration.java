package cool.scx.log;

import cool.scx.ScxAppRoot;
import cool.scx.config.ScxConfig;
import cool.scx.util.StringUtils;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class ScxLogConfiguration {

    private static final Map<String, ScxLoggerInfo> loggerInfoMap = new HashMap<>();
    private static ScxLoggerInfo rootScxLoggerInfo = null;

    public static void init(ScxConfig scxConfig, ScxAppRoot scxAppRoot) {
        var defaultLogFilePath = scxAppRoot.getFileByAppRoot("AppRoot:logs");

        rootScxLoggerInfo = new ScxLoggerInfo();
        rootScxLoggerInfo.name = "ScxLogRoot";
        rootScxLoggerInfo.level = Level.ERROR;
        rootScxLoggerInfo.type = ScxLoggerInfoType.CONSOLE;
        rootScxLoggerInfo.filePath = defaultLogFilePath;

        var rootLevelStr = scxConfig.get("scx.log.root.level", String.class);
        var rootTypeStr = scxConfig.get("scx.log.root.type", String.class);
        var rootFilePathStr = scxConfig.get("scx.log.root.file-path", String.class);
        fillScxLoggerInfo(scxAppRoot, rootLevelStr, rootTypeStr, rootFilePathStr, rootScxLoggerInfo);

        var loggers = scxConfig.getOrDefault("scx.log.loggers", new ArrayList<Map<String, ?>>());

        for (Map<String, ?> logger : loggers) {
            Object name_o = logger.get("name");
            Object level_o = logger.get("level");
            Object type_o = logger.get("type");
            Object filePath_o = logger.get("file-path");

            String name = name_o != null ? name_o.toString() : null;
            String level = level_o != null ? level_o.toString() : null;
            String type = type_o != null ? type_o.toString() : null;
            String filePath = filePath_o != null ? filePath_o.toString() : null;

            if (StringUtils.isNotBlank(name)) {
                var tempScxLoggerInfo = new ScxLoggerInfo();
                tempScxLoggerInfo.name = name;
                tempScxLoggerInfo.level = rootScxLoggerInfo.level;
                tempScxLoggerInfo.type = rootScxLoggerInfo.type;
                tempScxLoggerInfo.filePath = rootScxLoggerInfo.filePath;
                fillScxLoggerInfo(scxAppRoot, level, type, filePath, tempScxLoggerInfo);
                loggerInfoMap.put(name, tempScxLoggerInfo);
            }

        }

    }

    private static void fillScxLoggerInfo(ScxAppRoot scxAppRoot, String level, String type, String filePath, ScxLoggerInfo tempScxLoggerInfo) {
        if (StringUtils.isNotBlank(level)) {
            try {
                tempScxLoggerInfo.level = Level.valueOf(level.trim().toUpperCase());
            } catch (Exception ignored) {
            }
        }
        if (StringUtils.isNotBlank(type)) {
            try {
                tempScxLoggerInfo.type = ScxLoggerInfoType.valueOf(type.trim().toUpperCase());
            } catch (Exception ignored) {

            }
        }
        if (StringUtils.isNotBlank(filePath)) {
            tempScxLoggerInfo.filePath = scxAppRoot.getFileByAppRoot(filePath);
        }
    }

    public static ScxLoggerInfo getLoggerInfo(String name) {
        return loggerInfoMap.getOrDefault(name, rootScxLoggerInfo);
    }

}
