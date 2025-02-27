package cool.scx.logging.spi.jdk;

import cool.scx.logging.ScxLoggerFactory;

import java.lang.System.Logger;
import java.lang.System.LoggerFinder;

/// ScxJDKLoggerFinder
///
/// @author scx567888
/// @version 0.0.1
public final class ScxJDKLoggerFinder extends LoggerFinder {

    @Override
    public Logger getLogger(String name, Module module) {
        return new ScxJDKLogger(ScxLoggerFactory.getLogger(name));
    }

}


