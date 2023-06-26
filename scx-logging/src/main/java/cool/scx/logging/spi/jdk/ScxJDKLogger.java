package cool.scx.logging.spi.jdk;

import cool.scx.logging.ScxLogger;
import cool.scx.logging.ScxLoggerFactory;

import java.util.ResourceBundle;

import static org.slf4j.helpers.MessageFormatter.arrayFormat;
import static org.slf4j.helpers.NormalizedParameters.getThrowableCandidate;
import static org.slf4j.helpers.NormalizedParameters.trimmedCopy;

public class ScxJDKLogger implements System.Logger {

    private final ScxLogger scxLogger;

    public ScxJDKLogger(String name) {
        this.scxLogger = ScxLoggerFactory.getLogger(name);
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
                this.scxLogger.log(level, arrayFormat(format, trimmedCopy).getMessage(), throwableCandidate);
            } else {
                this.scxLogger.log(level, arrayFormat(format, params).getMessage(), null);
            }
        }
    }

}
