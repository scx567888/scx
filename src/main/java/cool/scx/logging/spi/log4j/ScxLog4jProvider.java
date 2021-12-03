package cool.scx.logging.spi.log4j;

import org.apache.logging.log4j.spi.Provider;

/**
 * a
 */
public class ScxLog4jProvider extends Provider {

    /**
     * a
     */
    public ScxLog4jProvider() {
        super(10, "2.6.0", ScxLog4jLoggerContextFactory.class);
    }

}
