package cool.scx.logging.spi.log4j;

import org.apache.logging.log4j.spi.Provider;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxLog4jProvider extends Provider {

    /**
     * a
     */
    public ScxLog4jProvider() {
        super(10, "2.6.0", ScxLog4jLoggerContextFactory.class);
    }

}
