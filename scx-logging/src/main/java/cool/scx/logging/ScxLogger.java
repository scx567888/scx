package cool.scx.logging;

import java.time.LocalDateTime;
import java.util.Set;

import static cool.scx.logging.ScxLoggerFactory.defaultConfig;
import static cool.scx.logging.ScxLoggerHelper.getFilteredStackTrace;
import static java.lang.System.Logger.Level;

/**
 * ScxLogger
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ScxLogger {

    private final String name;

    private final ScxLoggerConfig config;

    /**
     * <p>Constructor for ScxLogger.</p>
     *
     * @param name a {@link java.lang.String} object
     */
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
    public void logMessage(Level level, String msg, Throwable throwable) {
        if (shouldSkip(level)) {
            return;
        }
        var now = LocalDateTime.now();
        //堆栈跟踪对象
        var contextStack = stackTrace() ? getFilteredStackTrace(new Exception()) : null;
        // 格式化 message
        var logRecord = new ScxLogRecord(now, level, this.name, msg, Thread.currentThread().getName(), throwable, contextStack);

        var recorders = recorders();

        for (var r : recorders) {
            r.record(logRecord);
        }

    }

    /**
     * <p>shouldSkip.</p>
     *
     * @param level a
     * @return a boolean
     */
    protected boolean shouldSkip(Level level) {
        return level == Level.OFF;
    }

    /**
     * <p>config.</p>
     *
     * @return a {@link cool.scx.logging.ScxLoggerConfig} object
     */
    public ScxLoggerConfig config() {
        return config;
    }

    /**
     * a
     *
     * @return a
     */
    public Level level() {
        return config.level();
    }

    /**
     * <p>stackTrace.</p>
     *
     * @return a boolean
     */
    public boolean stackTrace() {
        return config.stackTrace();
    }

    /**
     * <p>recorders.</p>
     *
     * @return a {@link java.util.Set} object
     */
    public Set<ScxLogRecorder> recorders() {
        return config.recorders();
    }

    /**
     * <p>name.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String name() {
        return name;
    }

}
