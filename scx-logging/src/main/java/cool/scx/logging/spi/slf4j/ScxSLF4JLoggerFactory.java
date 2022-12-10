package cool.scx.logging.spi.slf4j;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxSLF4JLoggerFactory implements ILoggerFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getLogger(String name) {
        return new ScxSLF4JLogger(name);
    }

}
