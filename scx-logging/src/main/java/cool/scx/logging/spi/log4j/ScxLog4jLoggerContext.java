package cool.scx.logging.spi.log4j;

import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxLog4jLoggerContext implements LoggerContext {

    /**
     * a
     */
    public ScxLog4jLoggerContext() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedLogger getLogger(final String name) {
        return this.getLogger(name, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedLogger getLogger(final String name, final MessageFactory messageFactory) {
        return new ScxLog4jLogger(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLogger(final String name) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLogger(final String name, final MessageFactory messageFactory) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLogger(final String name, final Class<? extends MessageFactory> messageFactoryClass) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getExternalContext() {
        return null;
    }

}
