package cool.scx.logging.spi.log4j;

import cool.scx.logging.ScxLogger;
import cool.scx.logging.ScxLoggerFactory;
import cool.scx.logging.ScxLoggingLevel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.spi.AbstractLogger;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxLog4jLogger extends AbstractLogger {

    /**
     * a
     */
    private final ScxLogger scxLogger;

    /**
     * a
     *
     * @param name a
     */
    public ScxLog4jLogger(final String name) {
        super(name);
        this.scxLogger = ScxLoggerFactory.getLogger(name);
    }

    /**
     * <p>toScxLoggingLevel.</p>
     *
     * @param level a {@link org.apache.logging.log4j.Level} object
     * @return a {@link cool.scx.logging.ScxLoggingLevel} object
     */
    private static ScxLoggingLevel toScxLoggingLevel(Level level) {
        return switch (level.toString()) {
            case "OFF" -> ScxLoggingLevel.OFF;
            case "FATAL" -> ScxLoggingLevel.FATAL;
            case "ERROR" -> ScxLoggingLevel.ERROR;
            case "WARN" -> ScxLoggingLevel.WARN;
            case "INFO" -> ScxLoggingLevel.INFO;
            case "DEBUG" -> ScxLoggingLevel.DEBUG;
            case "TRACE" -> ScxLoggingLevel.TRACE;
            case "ALL" -> ScxLoggingLevel.ALL;
            default -> throw new IllegalArgumentException();
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
        this.scxLogger.logMessage(toScxLoggingLevel(mgsLevel), message.getFormattedMessage(), throwable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Level getLevel() {
        return switch (this.scxLogger.level()) {
            case OFF -> Level.OFF;
            case FATAL -> Level.FATAL;
            case ERROR -> Level.ERROR;
            case WARN -> Level.WARN;
            case INFO -> Level.INFO;
            case DEBUG -> Level.DEBUG;
            case TRACE -> Level.TRACE;
            case ALL -> Level.ALL;
        };
    }

}
