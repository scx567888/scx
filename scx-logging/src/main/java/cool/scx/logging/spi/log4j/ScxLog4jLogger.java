package cool.scx.logging.spi.log4j;

import cool.scx.logging.ScxLogger;
import cool.scx.logging.ScxLoggerFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.spi.AbstractLogger;

import static org.apache.logging.log4j.Level.*;

/**
 * ScxLog4jLogger
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxLog4jLogger extends AbstractLogger {

    /**
     * 内部的 scxLogger
     */
    private final ScxLogger scxLogger;

    /**
     * 名称
     *
     * @param name a
     */
    public ScxLog4jLogger(final String name) {
        super(name);
        this.scxLogger = ScxLoggerFactory.getLogger(name);
    }

    /**
     * Log4jLevel 转 ScxLoggingLevel
     *
     * @param level 级别
     * @return ScxLoggingLevel
     */
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

    /**
     * ScxLoggingLevel 转 Log4jLevel
     *
     * @param level level
     * @return Level
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final Message msg, final Throwable t) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final CharSequence msg, final Throwable t) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final Object msg, final Throwable t) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String msg) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String msg, final Object... p1) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String msg, final Throwable t) {
        return getLevel().intLevel() >= testLevel.intLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logMessage(final String fqcn, final Level mgsLevel, final Marker marker, final Message message, final Throwable throwable) {
        this.scxLogger.logMessage(toJDKLevel(mgsLevel), message.getFormattedMessage(), throwable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Level getLevel() {
        return toLog4jLevel(this.scxLogger.level());
    }

}
