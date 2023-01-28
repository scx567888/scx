package cool.scx.logging.spi.log4j;

import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

import java.net.URI;

/**
 * ScxLog4jLoggerContextFactory
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxLog4jLoggerContextFactory implements LoggerContextFactory {

    /**
     * Constant <code>context</code>
     */
    private static final ScxLog4jLoggerContext CONTEXT = new ScxLog4jLoggerContext();

    /**
     * {@inheritDoc}
     */
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final Object externalContext, final boolean currentContext) {
        return CONTEXT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final Object externalContext, final boolean currentContext, final URI configLocation, final String name) {
        return CONTEXT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeContext(final LoggerContext removeContext) {

    }

}
