package cool.scx.logging.spi.log4j;

import cool.scx.logging.ScxLogger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.spi.AbstractLogger;

import static org.apache.logging.log4j.Level.*;
import static org.apache.logging.log4j.Level.ALL;

/// ScxLog4jLogger
///
/// @author scx567888
/// @version 0.0.1
public final class ScxLog4jLogger extends AbstractLogger {

    private final ScxLogger scxLogger;

    public ScxLog4jLogger(ScxLogger scxLogger) {
        super(scxLogger.name());
        this.scxLogger = scxLogger;
    }

    private boolean isLoggable(Level level) {
        return scxLogger.isLoggable(toJDKLevel(level));
    }

    @Override
    public Level getLevel() {
        return toLog4jLevel(scxLogger.config().level());
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
        return isLoggable(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable t) {
        return isLoggable(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
        return isLoggable(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
        return isLoggable(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message) {
        return isLoggable(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
        return isLoggable(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0) {
        return isLoggable(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1) {
        return isLoggable(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
        return isLoggable(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        return isLoggable(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        return isLoggable(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        return isLoggable(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        return isLoggable(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        return isLoggable(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        return isLoggable(level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        return isLoggable(level);
    }

    @Override
    public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
        // logMessage 调用前一定会执行 isEnabled 判断, 所以此处直接调用 log0
        scxLogger.log0(toJDKLevel(level), message.getFormattedMessage(), t);
    }

    private static System.Logger.Level toJDKLevel(Level level) {
        if (level == OFF) {
            return System.Logger.Level.OFF;
        }
        if (level == FATAL || level == ERROR) {
            return System.Logger.Level.ERROR;
        }
        if (level == WARN) {
            return System.Logger.Level.WARNING;
        }
        if (level == INFO) {
            return System.Logger.Level.INFO;
        }
        if (level == DEBUG) {
            return System.Logger.Level.DEBUG;
        }
        if (level == TRACE) {
            return System.Logger.Level.TRACE;
        }
        if (level == ALL) {
            return System.Logger.Level.ALL;
        }
        throw new IllegalArgumentException();
    }

    private static Level toLog4jLevel(System.Logger.Level level) {
        return switch (level) {
            case OFF -> Level.OFF;
            case ERROR -> Level.ERROR;
            case WARNING -> Level.WARN;
            case INFO -> Level.INFO;
            case DEBUG -> Level.DEBUG;
            case TRACE -> Level.TRACE;
            case ALL -> Level.ALL;
        };
    }

}
