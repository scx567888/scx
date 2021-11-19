package cool.scx.log.spi.log4j;

import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

import java.net.URI;

/**
 * a
 */
public final class ScxLog4jLoggerContextFactory implements LoggerContextFactory {

    private static final ScxLog4jLoggerContext context = new ScxLog4jLoggerContext();

    /**
     * a
     */
    public ScxLog4jLoggerContextFactory() {
    }

    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final Object externalContext, final boolean currentContext) {
        return context;
    }

    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final Object externalContext, final boolean currentContext, final URI configLocation, final String name) {
        return context;
    }

    @Override
    public void removeContext(final LoggerContext removeContext) {

    }

}
