package cool.scx.logging.spi.log4j;

import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;

/**
 * a
 */
public class ScxLog4jLoggerContext implements LoggerContext {

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
        return new ScxLog4jLogger(name);
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
