package cool.scx.logging;

import java.time.LocalDateTime;
import java.util.Set;

import static cool.scx.logging.ScxLoggerFactory.defaultConfig;
import static cool.scx.logging.ScxLoggerHelper.getFilteredStackTrace;

/**
 * ScxLogger
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ScxLogger {

    private final String name;

    private final ScxLoggerConfig config;

    public ScxLogger(String name) {
        this.name = name;
        //所有 ScxLogger 的 config 的 父 config 都是 ScxLoggerFactory.defaultConfig()
        this.config = new ScxLoggerConfig(defaultConfig());
    }

    /**
     * 打印日志方法
     *
     * @param level     a
     * @param msg       a
     * @param throwable a
     */
    public void logMessage(ScxLoggingLevel level, String msg, Throwable throwable) {
        if (shouldSkip(level)) {
            return;
        }
        var now = LocalDateTime.now();
        //堆栈跟踪对象
        var contextStack = stackTrace() ? getFilteredStackTrace(new Exception()) : null;
        // 格式化 message
        var logEvent = new ScxLogEvent(now, level, this.name, msg, Thread.currentThread().getName(), throwable, contextStack);

        var recorders = recorders();

        for (var r : recorders) {
            r.record(logEvent);
        }

    }

    protected boolean shouldSkip(ScxLoggingLevel level) {
        return level == ScxLoggingLevel.OFF;
    }

    public ScxLoggerConfig config() {
        return config;
    }

    public ScxLoggingLevel level() {
        return config.level();
    }

    private boolean stackTrace() {
        return config.stackTrace();
    }

    private Set<ScxLogRecorder> recorders() {
        return config.recorders();
    }

}
