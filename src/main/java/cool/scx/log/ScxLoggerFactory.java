package cool.scx.log;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public final class ScxLoggerFactory implements ILoggerFactory {

    @Override
    public Logger getLogger(String name) {
        return new ScxLogger(name);
    }

}
