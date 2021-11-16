package cool.scx.log.spi.slf4j;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public final class ScxSLF4JLoggerFactory implements ILoggerFactory {

    @Override
    public Logger getLogger(String name) {
        return new ScxSLF4JLogger(name);
    }

}
