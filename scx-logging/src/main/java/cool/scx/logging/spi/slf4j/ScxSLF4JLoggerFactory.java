package cool.scx.logging.spi.slf4j;

import cool.scx.logging.ScxLoggerFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public final class ScxSLF4JLoggerFactory implements ILoggerFactory {

    @Override
    public Logger getLogger(String name) {
        return new ScxSLF4JLogger(ScxLoggerFactory.getLogger(name));
    }

}
