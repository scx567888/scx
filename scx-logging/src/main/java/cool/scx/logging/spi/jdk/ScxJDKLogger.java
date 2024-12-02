package cool.scx.logging.spi.jdk;

import cool.scx.logging.ScxLogger;

import java.lang.System.Logger;
import java.util.ResourceBundle;

import static cool.scx.logging.spi.jdk.ScxJDKLoggerHelper.*;

/**
 * ScxJDKLogger
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxJDKLogger implements Logger {

    private final ScxLogger scxLogger;

    public ScxJDKLogger(ScxLogger scxLogger) {
        this.scxLogger = scxLogger;
    }

    @Override
    public String getName() {
        return scxLogger.name();
    }

    @Override
    public boolean isLoggable(Level level) {
        return scxLogger.isLoggable(level);
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable t) {
        scxLogger.log(level, getResourceString(bundle, msg), t);
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        scxLogger.log(level, formatMessage(getResourceString(bundle, format), params), getThrowableCandidate(params));
    }

}
