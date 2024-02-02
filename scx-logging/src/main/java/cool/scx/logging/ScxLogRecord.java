package cool.scx.logging;

import java.lang.System.Logger.Level;
import java.time.LocalDateTime;

public record ScxLogRecord(LocalDateTime timeStamp,
                           Level level,
                           String loggerName,
                           String message,
                           String threadName,
                           Throwable throwable,
                           StackTraceElement[] contextStack) {

}
