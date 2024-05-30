package cool.scx.logging.spi.log4j;

import org.apache.logging.log4j.spi.Provider;

public final class ScxLog4jProvider extends Provider {

    public ScxLog4jProvider() {
        super(10, "2.6.0", ScxLog4jLoggerContextFactory.class);
    }

}
