package cool.scx.logging.spi.slf4j;

import cool.scx.logging.ScxLogger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.LegacyAbstractLogger;

import static cool.scx.logging.spi.slf4j.ScxSLF4JLoggerHelper.toJDKLevel;
import static org.slf4j.event.Level.*;
import static org.slf4j.helpers.MessageFormatter.basicArrayFormat;

/// ScxSLF4JLogger
///
/// @author scx567888
/// @version 0.0.1
public final class ScxSLF4JLogger extends LegacyAbstractLogger {

    private final ScxLogger scxLogger;

    public ScxSLF4JLogger(ScxLogger scxLogger) {
        this.name = scxLogger.name();
        this.scxLogger = scxLogger;
    }

    private boolean isLoggable(Level level) {
        return scxLogger.isLoggable(toJDKLevel(level));
    }

    @Override
    public boolean isTraceEnabled() {
        return isLoggable(TRACE);
    }

    @Override
    public boolean isDebugEnabled() {
        return isLoggable(DEBUG);
    }

    @Override
    public boolean isInfoEnabled() {
        return isLoggable(INFO);
    }

    @Override
    public boolean isWarnEnabled() {
        return isLoggable(WARN);
    }

    @Override
    public boolean isErrorEnabled() {
        return isLoggable(ERROR);
    }

    @Override
    protected String getFullyQualifiedCallerName() {
        return null;
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String msg, Object[] args, Throwable t) {
        scxLogger.log0(toJDKLevel(level), basicArrayFormat(msg, args), t);
    }

}
