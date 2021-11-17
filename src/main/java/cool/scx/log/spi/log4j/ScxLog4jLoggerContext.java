package cool.scx.log.spi.log4j;

import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;

import java.util.HashMap;
import java.util.Map;

public class ScxLog4jLoggerContext implements LoggerContext {

    private final Map<String, ExtendedLogger> loggerCache = new HashMap<>();

    public ScxLog4jLoggerContext() {

    }

    public ExtendedLogger getLogger(final String name) {
        return this.getLogger(name, null);
    }

    public ExtendedLogger getLogger(final String name, final MessageFactory messageFactory) {
        var extendedLogger = this.loggerCache.get(name);
        if (extendedLogger != null) {
            return extendedLogger;
        } else {
            var scxLog4jLogger = new ScxLog4jLogger(name);
            this.loggerCache.putIfAbsent(name, scxLog4jLogger);
            return this.loggerCache.get(name);
        }
    }

    public boolean hasLogger(final String name) {
        return false;
    }

    public boolean hasLogger(final String name, final MessageFactory messageFactory) {
        return false;
    }

    public boolean hasLogger(final String name, final Class<? extends MessageFactory> messageFactoryClass) {
        return false;
    }

    public Object getExternalContext() {
        return null;
    }

}
