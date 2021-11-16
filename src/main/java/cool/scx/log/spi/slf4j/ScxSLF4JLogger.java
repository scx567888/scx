package cool.scx.log.spi.slf4j;

import cool.scx.log.ScxLogConfiguration;
import cool.scx.log.ScxLogHelper;
import cool.scx.log.ScxLoggerInfo;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.LegacyAbstractLogger;
import org.slf4j.helpers.MessageFormatter;

public final class ScxSLF4JLogger extends LegacyAbstractLogger {

    private final ScxLoggerInfo scxLoggerInfo;

    public ScxSLF4JLogger(String name) {
        this.name = name;
        this.scxLoggerInfo = ScxLogConfiguration.getLoggerInfo(name);
    }

    @Override
    protected String getFullyQualifiedCallerName() {
        return null;
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String message, Object[] arguments, Throwable throwable) {
        var levelStr = (level == Level.INFO || level == Level.WARN) ? level + " " : level.toString();
        var msg = MessageFormatter.arrayFormat(message, arguments).getMessage();
        ScxLogHelper.logMessage(name, levelStr, msg, scxLoggerInfo, throwable);
    }

    @Override
    public boolean isTraceEnabled() {
        return scxLoggerInfo.level.toInt() <= Level.TRACE.toInt();
    }

    @Override
    public boolean isDebugEnabled() {
        return scxLoggerInfo.level.toInt() <= Level.DEBUG.toInt();
    }

    @Override
    public boolean isInfoEnabled() {
        return scxLoggerInfo.level.toInt() <= Level.INFO.toInt();
    }

    @Override
    public boolean isWarnEnabled() {
        return scxLoggerInfo.level.toInt() <= Level.WARN.toInt();
    }

    @Override
    public boolean isErrorEnabled() {
        return scxLoggerInfo.level.toInt() <= Level.ERROR.toInt();
    }

}
