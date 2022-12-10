package cool.scx.logging.spi.log4j;

import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

import java.net.URI;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxLog4jLoggerContextFactory implements LoggerContextFactory {

    /**
     * Constant <code>context</code>
     */
    private static final ScxLog4jLoggerContext context = new ScxLog4jLoggerContext();

    /**
     * a
     */
    public ScxLog4jLoggerContextFactory() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final Object externalContext, final boolean currentContext) {
        return context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final Object externalContext, final boolean currentContext, final URI configLocation, final String name) {
        return context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeContext(final LoggerContext removeContext) {

    }

}
