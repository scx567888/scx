package cool.scx.log.spi.log4j;

import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerRegistry;

public class ScxLog4jLoggerContext implements LoggerContext {

    private final LoggerRegistry<ExtendedLogger> loggerRegistry = new LoggerRegistry<>();

    public ScxLog4jLoggerContext() {

    }

    public ExtendedLogger getLogger(final String name) {
        return this.getLogger(name, null);
    }

    public ExtendedLogger getLogger(final String name, final MessageFactory messageFactory) {
        ExtendedLogger extendedLogger = this.loggerRegistry.getLogger(name, messageFactory);
        if (extendedLogger != null) {
            AbstractLogger.checkMessageFactory(extendedLogger, messageFactory);
            return extendedLogger;
        } else {
            var scxLog4jLogger = new ScxLog4jLogger(name, messageFactory);
            this.loggerRegistry.putIfAbsent(name, messageFactory, scxLog4jLogger);
            return this.loggerRegistry.getLogger(name, messageFactory);
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
