package cool.scx.logging.spi.jdk;

import cool.scx.logging.ScxLogger;
import cool.scx.logging.ScxLoggerFactory;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class ScxJDKLogger implements System.Logger {

    private final ScxLogger scxLogger;

    public ScxJDKLogger(String name) {
        this.scxLogger = ScxLoggerFactory.getLogger(name);
    }

    public static Object[] trimmedCopy(Object[] argArray) {
        if (argArray != null && argArray.length != 0) {
            int trimmedLen = argArray.length - 1;
            Object[] trimmed = new Object[trimmedLen];
            if (trimmedLen > 0) {
                System.arraycopy(argArray, 0, trimmed, 0, trimmedLen);
            }

            return trimmed;
        } else {
            throw new IllegalStateException("non-sensical empty or null argument array");
        }
    }

    public static Throwable getThrowableCandidate(Object[] argArray) {
        if (argArray != null && argArray.length != 0) {
            Object lastEntry = argArray[argArray.length - 1];
            return lastEntry instanceof Throwable ? (Throwable) lastEntry : null;
        } else {
            return null;
        }
    }

    @Override
    public String getName() {
        return this.scxLogger.name();
    }

    @Override
    public boolean isLoggable(Level level) {
        return level.getSeverity() >= this.scxLogger.level().getSeverity();
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
        if (isLoggable(level)) {
            this.scxLogger.log(level, msg, thrown);
        }
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        if (isLoggable(level)) {
            var throwableCandidate = getThrowableCandidate(params);
            if (throwableCandidate != null) {
                var trimmedCopy = trimmedCopy(params);
                this.scxLogger.log(level, MessageFormat.format(format, trimmedCopy), throwableCandidate);
            } else {
                this.scxLogger.log(level, MessageFormat.format(format, params), null);
            }
        }
    }

}
