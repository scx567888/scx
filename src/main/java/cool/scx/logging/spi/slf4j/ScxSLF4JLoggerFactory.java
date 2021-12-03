package cool.scx.logging.spi.slf4j;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * a
 */
public final class ScxSLF4JLoggerFactory implements ILoggerFactory {

    @Override
    public Logger getLogger(String name) {
        return new ScxSLF4JLogger(name);
    }

}
