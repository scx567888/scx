package cool.scx.logging;

import cool.scx.logging.recorder.ConsoleRecorder;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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

    private static final ScxLoggerConfig ROOT_CONFIG = new ScxLoggerConfig();

    // 存储所有日志
    private static final ConcurrentHashMap<String, ScxLogger> LOGGERS = new ConcurrentHashMap<>();

    // 存储所有配置
    private static final LinkedHashMap<String, ScxLoggerConfig> CONFIGS = new LinkedHashMap<>();

    // 保护 CONFIGS 的锁
    private static final ReentrantReadWriteLock CONFIGS_LOCK = new ReentrantReadWriteLock();

    /// 根配置 可修改此配置来影响根配置
    public static ScxLoggerConfig rootConfig() {
        return ROOT_CONFIG;
    }

    private static ScxLoggerConfig findConfig(String name) {
        CONFIGS_LOCK.readLock().lock();
        try {
            for (var entry : CONFIGS.entrySet()) {
                var b = Pattern.matches(entry.getKey(), name);
                if (b) {
                    return entry.getValue();
                }
            }
            return null;
        } finally {
            CONFIGS_LOCK.readLock().unlock();
        }
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

    public static void setConfig(String name, ScxLoggerConfig newConfig) {
        CONFIGS_LOCK.writeLock().lock();
        try {
            //设置未来的日志配置, 倒序插入保证遍历的时候 新配置永远在前
            CONFIGS.putFirst(name, newConfig);
            // 更新现有 Logger 的配置
            for (var value : LOGGERS.values()) {
                var b = Pattern.matches(name, value.name());
                if (b) {
                    value.config().updateConfig(newConfig);
                }
            }
        } finally {
            CONFIGS_LOCK.writeLock().unlock();
        }
    }

    public static void removeConfig(String name) {
        CONFIGS_LOCK.writeLock().lock();
        try {
            CONFIGS.remove(name);
        } finally {
            CONFIGS_LOCK.writeLock().unlock();
        }
    }

}
