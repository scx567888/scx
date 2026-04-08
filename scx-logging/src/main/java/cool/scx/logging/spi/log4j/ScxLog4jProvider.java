package cool.scx.logging.spi.log4j;

import org.apache.logging.log4j.spi.Provider;

/// ScxLog4jProvider
///
/// @author scx567888
/// @version 0.0.1
public final class ScxLog4jProvider extends Provider {

    public ScxLog4jProvider() {
        super(10, CURRENT_VERSION, ScxLog4jLoggerContextFactory.class);
    }

}
