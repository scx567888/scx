package cool.scx.log.spi.log4j;

import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

import java.net.URI;

public final class ScxLog4jLoggerContextFactory implements LoggerContextFactory {

    private static final ScxLog4jLoggerContext context = new ScxLog4jLoggerContext();

    public ScxLog4jLoggerContextFactory() {
    }

    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final Object externalContext, final boolean currentContext) {
        return context;
    }

    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final Object externalContext, final boolean currentContext, final URI configLocation, final String name) {
        return context;
    }

    public void removeContext(final LoggerContext removeContext) {

    }

}
