package cool.scx.logging.spi.log4j;

import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;

/**
 * ScxLog4jLoggerContext
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxLog4jLoggerContext implements LoggerContext {

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedLogger getLogger(final String name) {
        return new ScxLog4jLogger(name);
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
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLogger(final String name, final MessageFactory messageFactory) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLogger(final String name, final Class<? extends MessageFactory> messageFactoryClass) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getExternalContext() {
        return null;
    }

}
