package cool.scx.log.spi.log4j;

import cool.scx.log.ScxLogConfiguration;
import cool.scx.log.ScxLogHelper;
import cool.scx.log.ScxLoggerInfo;
import cool.scx.util.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;

public class ScxLog4jLogger extends AbstractLogger {
    private final Level level;

    private final ScxLoggerInfo scxLoggerInfo;

    public ScxLog4jLogger(final String name, final MessageFactory messageFactory) {
        super(name, messageFactory);
        this.scxLoggerInfo = ScxLogConfiguration.getLoggerInfo(name);
        this.level = Level.toLevel(this.scxLoggerInfo.level.name(), Level.ERROR);
    }

    public Level getLevel() {
        return this.level;
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final Message msg, final Throwable t) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final CharSequence msg, final Throwable t) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final Object msg, final Throwable t) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final String msg) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final String msg, final Object... p1) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public boolean isEnabled(final Level testLevel, final Marker marker, final String msg, final Throwable t) {
        return this.level.intLevel() >= testLevel.intLevel();
    }

    public void logMessage(final String fqcn, final Level mgsLevel, final Marker marker, final Message message, final Throwable throwable) {
        var levelStr = (level == Level.INFO || level == Level.WARN) ? level + " " : level.toString();
        var msg = message.getFormattedMessage();
        if (throwable != null) {
            msg = msg + "Throwable : " + ExceptionUtils.getStackTrace(throwable);
        }
        ScxLogHelper.logMessage(name, levelStr, msg, scxLoggerInfo);
    }

}
