package cool.scx.logging.spi.log4j;

import org.apache.logging.log4j.Level;

import static org.apache.logging.log4j.Level.*;


/// ScxLog4jLoggerHelper
///
/// @author scx567888
/// @version 0.0.1
final class ScxLog4jLoggerHelper {

    public static System.Logger.Level toJDKLevel(Level level) {
        if (level == OFF) {
            return System.Logger.Level.OFF;
        }
        if (level == FATAL || level == ERROR) {
            return System.Logger.Level.ERROR;
        }
        if (level == WARN) {
            return System.Logger.Level.WARNING;
        }
        if (level == INFO) {
            return System.Logger.Level.INFO;
        }
        if (level == DEBUG) {
            return System.Logger.Level.DEBUG;
        }
        if (level == TRACE) {
            return System.Logger.Level.TRACE;
        }
        if (level == ALL) {
            return System.Logger.Level.ALL;
        }
        throw new IllegalArgumentException();
    }

    public static Level toLog4jLevel(System.Logger.Level level) {
        return switch (level) {
            case OFF -> Level.OFF;
            case ERROR -> Level.ERROR;
            case WARNING -> Level.WARN;
            case INFO -> Level.INFO;
            case DEBUG -> Level.DEBUG;
            case TRACE -> Level.TRACE;
            case ALL -> Level.ALL;
        };
    }

}
