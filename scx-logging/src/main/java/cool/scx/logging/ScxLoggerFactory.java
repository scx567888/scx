package cool.scx.logging;

import cool.scx.logging.recorder.ConsoleRecorder;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import static java.lang.System.Logger.Level.ERROR;

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
    private static final ReentrantLock CONFIGS_LOCK = new ReentrantLock();

    /// 根配置 可修改此配置来影响根配置
    public static ScxLoggerConfig rootConfig() {
        return ROOT_CONFIG;
    }

    private static ScxLoggerConfig findConfig(String name) {
        CONFIGS_LOCK.lock();
        try {
            for (var entry : CONFIGS.entrySet()) {
                var b = Pattern.matches(entry.getKey(), name);
                if (b) {
                    return entry.getValue();
                }
            }
            return null;
        } finally {
            CONFIGS_LOCK.unlock();
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

    // 去除了 synchronized，利用 ConcurrentHashMap 的线程安全性
    public static ScxLogger getLogger(String name) {
        return LOGGERS.computeIfAbsent(name, ScxLoggerFactory::createLogger);
    }

    public static ScxLogger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    // 使用显式锁保护 CONFIGS 的更新
    public static void setConfig(String name, ScxLoggerConfig newConfig) {
        CONFIGS_LOCK.lock();
        try {
            CONFIGS.putFirst(name, newConfig);
            // 更新现有 Logger 的配置
            for (var value : LOGGERS.values()) {
                boolean b = Pattern.matches(name, value.name());
                if (b) {
                    value.config().updateConfig(newConfig);
                }
            }
        } finally {
            CONFIGS_LOCK.unlock();
        }
    }

    public static void removeConfig(String name) {
        CONFIGS_LOCK.lock();
        try {
            CONFIGS.remove(name);
        } finally {
            CONFIGS_LOCK.unlock();
        }
    }

}
