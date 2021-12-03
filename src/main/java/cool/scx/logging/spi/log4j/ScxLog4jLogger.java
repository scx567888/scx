package cool.scx.logging.spi.log4j;

import cool.scx.logging.ScxLogConfiguration;
import cool.scx.logging.ScxLogHelper;
import cool.scx.logging.ScxLogLevel;
import cool.scx.logging.ScxLoggerInfo;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.spi.AbstractLogger;

/**
 * a
 */
public final class ScxLog4jLogger extends AbstractLogger {

    /**
     * a
     */
    private final ScxLoggerInfo scxLoggerInfo;

    /**
     * a
     */
    private final Level level;

    /**
     * a
     *
     * @param name a
     */
    public ScxLog4jLogger(final String name) {
        super(name);
        this.scxLoggerInfo = ScxLogConfiguration.getLoggerInfo(name);
        this.level = this.scxLoggerInfo.level().toLog4jLevel();
    }

    @Override
    public Level getLevel() {
        return this.level;
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final Message msg, final Throwable t) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final CharSequence msg, final Throwable t) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final Object msg, final Throwable t) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String msg) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String msg, final Object... p1) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public boolean isEnabled(final Level testLevel, final Marker marker, final String msg, final Throwable t) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    @Override
    public void logMessage(final String fqcn, final Level mgsLevel, final Marker marker, final Message message, final Throwable throwable) {
        ScxLogHelper.logMessage(name, ScxLogLevel.of(mgsLevel), message.getFormattedMessage(), scxLoggerInfo, throwable);
    }

}
