package cool.scx.logging.spi.jdk;

import java.lang.System.Logger;

public final class ScxJDKLoggerFinder extends System.LoggerFinder {

    @Override
    public Logger getLogger(String name, Module module) {
        return new ScxJDKLogger(name);
    }

}


