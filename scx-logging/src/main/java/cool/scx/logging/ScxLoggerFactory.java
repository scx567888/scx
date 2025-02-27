package cool.scx.logging;

import cool.scx.logging.recorder.ConsoleRecorder;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static java.lang.System.Logger.Level.ERROR;


/// ScxLoggerFactory
///
/// @author scx567888
/// @version 0.0.1
public final class ScxLoggerFactory {

    static final System.Logger.Level DEFAULT_LEVEL = ERROR;
    static final Boolean DEFAULT_STACK_TRACE = false;
    static final Set<ScxLogRecorder> DEFAULT_RECORDERS = Set.of(new ConsoleRecorder());

    private static final Map<String, ScxLogger> LOGGERS = new ConcurrentHashMap<>();
    private static final Map<String, ScxLoggerConfig> CONFIGS = new ConcurrentHashMap<>();
    private static final ScxLoggerConfig ROOT_CONFIG = new ScxLoggerConfig();

    /// 根配置 可修改此配置来影响根配置
    public static ScxLoggerConfig rootConfig() {
        return ROOT_CONFIG;
    }

    private static ScxLoggerConfig findConfig(String name) {
        for (var entry : CONFIGS.entrySet()) {
            var b = Pattern.matches(entry.getKey(), name);
            if (b) {
                return entry.getValue();
            }
        }
        return null;
    }

    private static ScxLogger createLogger(String name) {
        var scxLogger = new ScxLogger(name, new ScxLoggerConfig(ROOT_CONFIG));
        var config = findConfig(name);
        if (config != null) {
            scxLogger.config().updateConfig(config);
        }
        return scxLogger;
    }

    public static ScxLogger getLogger(String name) {
        return LOGGERS.computeIfAbsent(name, ScxLoggerFactory::createLogger);
    }

    public static ScxLogger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    /// 更新日志配置
    ///
    /// @param name      日志名称 也可以为正则表达式
    /// @param newConfig 新配置
    public static void setConfig(String name, ScxLoggerConfig newConfig) {
        //更新现有日志配置    
        for (var value : LOGGERS.values()) {
            var b = Pattern.matches(name, value.name());
            if (b) {
                value.config().updateConfig(newConfig);
            }
        }
        //设置未来的日志配置
        CONFIGS.put(name, newConfig);
    }

    /// 移除日志配置 , 已存在的日志对象不会收到影响
    ///
    /// @param name 日志名称 也可以为正则表达式
    public static void removeConfig(String name) {
        CONFIGS.remove(name);
    }

}
