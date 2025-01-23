package cool.scx.socket2.test;

import cool.scx.logging.ScxLoggerFactory;

public class InitLogger {

    static {
        ScxLoggerFactory.rootConfig().setLevel(System.Logger.Level.DEBUG);
    }

}
