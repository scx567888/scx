package cool.scx.log.spi.slf4j;

import cool.scx.log.ScxLogConfiguration;
import cool.scx.log.ScxLogHelper;
import cool.scx.log.ScxLogLevel;
import cool.scx.log.ScxLoggerInfo;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.LegacyAbstractLogger;
import org.slf4j.helpers.MessageFormatter;

/**
 * a
 */
public final class ScxSLF4JLogger extends LegacyAbstractLogger {

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
    public ScxSLF4JLogger(String name) {
        this.name = name;
        this.scxLoggerInfo = ScxLogConfiguration.getLoggerInfo(name);
        this.level = this.scxLoggerInfo.level().toSLF4JLevel();
    }

    @Override
    protected String getFullyQualifiedCallerName() {
        return null;
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String message, Object[] arguments, Throwable throwable) {
        ScxLogHelper.logMessage(name, ScxLogLevel.of(level), MessageFormatter.arrayFormat(message, arguments).getMessage(), scxLoggerInfo, throwable);
    }

    @Override
    public boolean isTraceEnabled() {
        return level.toInt() <= Level.TRACE.toInt();
    }

    @Override
    public boolean isDebugEnabled() {
        return level.toInt() <= Level.DEBUG.toInt();
    }

    @Override
    public boolean isInfoEnabled() {
        return level.toInt() <= Level.INFO.toInt();
    }

    @Override
    public boolean isWarnEnabled() {
        return level.toInt() <= Level.WARN.toInt();
    }

    @Override
    public boolean isErrorEnabled() {
        return level.toInt() <= Level.ERROR.toInt();
    }

}
