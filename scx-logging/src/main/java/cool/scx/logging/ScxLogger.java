package cool.scx.logging;

import java.time.LocalDateTime;

import static cool.scx.logging.ScxLoggerHelper.getFilteredStackTrace;
import static java.lang.System.Logger.Level;


/// ScxLogger
///
/// @author scx567888
/// @version 0.0.1
public record ScxLogger(String name, ScxLoggerConfig config) {

    public boolean isLoggable(Level level) {
        return level.getSeverity() >= config.level().getSeverity();
    }

    public void log(Level level, String message, Throwable t) {
        if (isLoggable(level)) {
            log0(level, message, t);
        }
    }

    public void log0(Level level, String message, Throwable t) {
        var now = LocalDateTime.now();
        var thread = Thread.currentThread();
        var contextStack = config.stackTrace() ? getFilteredStackTrace(new Exception()) : null;

        var logRecord = new ScxLogRecord(now, level, name, message, thread.getName(), t, contextStack);

        var recorders = config.recorders();

        for (var r : recorders) {
            r.record(logRecord);
        }
    }

}
