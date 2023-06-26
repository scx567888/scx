package cool.scx.logging.spi.slf4j;

import cool.scx.logging.ScxLogger;
import cool.scx.logging.ScxLoggerFactory;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.LegacyAbstractLogger;
import org.slf4j.helpers.MessageFormatter;

/**
 * ScxSLF4JLogger
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxSLF4JLogger extends LegacyAbstractLogger {

    /**
     * 内部的 scxLogger
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

    /**
     * SLF4JLevel 转 ScxLoggingLevel
     *
     * @param level a {@link org.slf4j.event.Level} object
     * @return a
     */
    private static System.Logger.Level toJDKLevel(Level level) {
        return switch (level) {
            case ERROR -> System.Logger.Level.ERROR;
            case WARN -> System.Logger.Level.WARNING;
            case INFO -> System.Logger.Level.INFO;
            case DEBUG -> System.Logger.Level.DEBUG;
            case TRACE -> System.Logger.Level.TRACE;
        };
    }

    /**
     * ScxLoggingLevel 转 SLF4JLevel
     *
     * @param level level
     * @return Level
     */
    private static Level toSLF4JLevel(System.Logger.Level level) {
        return switch (level) {
            case OFF, ERROR -> Level.ERROR;
            case WARNING -> Level.WARN;
            case INFO -> Level.INFO;
            case DEBUG -> Level.DEBUG;
            case TRACE, ALL -> Level.TRACE;
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFullyQualifiedCallerName() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String message, Object[] arguments, Throwable throwable) {
        this.scxLogger.logMessage(toJDKLevel(level), MessageFormatter.arrayFormat(message, arguments).getMessage(), throwable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTraceEnabled() {
        return getLevel().toInt() <= Level.TRACE.toInt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDebugEnabled() {
        return getLevel().toInt() <= Level.DEBUG.toInt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInfoEnabled() {
        return getLevel().toInt() <= Level.INFO.toInt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWarnEnabled() {
        return getLevel().toInt() <= Level.WARN.toInt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isErrorEnabled() {
        return getLevel().toInt() <= Level.ERROR.toInt();
    }

    /**
     * 获取级别
     *
     * @return a
     */
    public Level getLevel() {
        return toSLF4JLevel(this.scxLogger.level());
    }

}
