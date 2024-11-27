package cool.scx.socket_vertx.test;

import cool.scx.logging.ScxLoggerFactory;

public class InitLogger {

    static {
        ScxLoggerFactory.rootConfig().setLevel(System.Logger.Level.ERROR);
    }

}
