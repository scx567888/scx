package cool.scx.logging.spi.slf4j;

import cool.scx.logging.ScxLogger;
import cool.scx.logging.ScxLoggerFactory;
import cool.scx.logging.ScxLoggingLevel;
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
    private final ScxLogger scxLogger;

    /**
     * a
     *
     * @param name a
     */
    public ScxSLF4JLogger(String name) {
        this.name = name;
        this.scxLogger = ScxLoggerFactory.getLogger(name);
    }

    @Override
    protected String getFullyQualifiedCallerName() {
        return null;
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String message, Object[] arguments, Throwable throwable) {
        this.scxLogger.logMessage(ScxLoggingLevel.of(level.toString()), MessageFormatter.arrayFormat(message, arguments).getMessage(), throwable);
    }

    @Override
    public boolean isTraceEnabled() {
        return getLevel().toInt() <= Level.TRACE.toInt();
    }

    @Override
    public boolean isDebugEnabled() {
        return getLevel().toInt() <= Level.DEBUG.toInt();
    }

    @Override
    public boolean isInfoEnabled() {
        return getLevel().toInt() <= Level.INFO.toInt();
    }

    @Override
    public boolean isWarnEnabled() {
        return getLevel().toInt() <= Level.WARN.toInt();
    }

    @Override
    public boolean isErrorEnabled() {
        return getLevel().toInt() <= Level.ERROR.toInt();
    }

    public Level getLevel() {
        //因为 SLF4J 日志级别分类较少所以这里做一个处理
        if (this.scxLogger.level() == ScxLoggingLevel.OFF || this.scxLogger.level() == ScxLoggingLevel.FATAL) {
            return Level.ERROR;
        } else if (this.scxLogger.level() == ScxLoggingLevel.ALL) {
            return Level.TRACE;
        } else {
            return Level.valueOf(this.scxLogger.level().toString());
        }
    }

}
