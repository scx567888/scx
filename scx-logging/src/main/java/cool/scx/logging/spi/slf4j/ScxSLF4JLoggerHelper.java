package cool.scx.logging.spi.slf4j;

import org.slf4j.event.Level;

final class ScxSLF4JLoggerHelper {

    public static System.Logger.Level toJDKLevel(Level level) {
        return switch (level) {
            case ERROR -> System.Logger.Level.ERROR;
            case WARN -> System.Logger.Level.WARNING;
            case INFO -> System.Logger.Level.INFO;
            case DEBUG -> System.Logger.Level.DEBUG;
            case TRACE -> System.Logger.Level.TRACE;
        };
    }

}
