package cool.scx.log;

import java.nio.file.Path;

/**
 * a
 */
public record ScxLoggerInfo(String name, ScxLogLevel level, ScxLogLoggingType loggingType, Path storedDirectory) {

}
