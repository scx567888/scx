package cool.scx.log.spi.log4j;

import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * a
 */
public class ScxLog4jLoggerContext implements LoggerContext {

    private final Map<String, ExtendedLogger> loggerCache = new HashMap<>();

    /**
     * a
     */
    public ScxLog4jLoggerContext() {

    }

    @Override
    public ExtendedLogger getLogger(final String name) {
        return this.getLogger(name, null);
    }

    @Override
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

    @Override
    public boolean hasLogger(final String name) {
        return false;
    }

    @Override
    public boolean hasLogger(final String name, final MessageFactory messageFactory) {
        return false;
    }

    @Override
    public boolean hasLogger(final String name, final Class<? extends MessageFactory> messageFactoryClass) {
        return false;
    }

    @Override
    public Object getExternalContext() {
        return null;
    }

}
