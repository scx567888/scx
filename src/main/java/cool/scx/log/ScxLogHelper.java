package cool.scx.log;

import cool.scx.util.ExceptionUtils;
import cool.scx.util.FileUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScxLogHelper {

    private static final DateTimeFormatter LOG_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static void logMessage(String name, String levelStr, String msg, ScxLoggerInfo scxLoggerInfo, Throwable throwable) {
        var nowTimeStr = ScxLogHelper.LOG_DATETIME_FORMATTER.format(LocalDateTime.now());
        var threadName = Thread.currentThread().getName();
        var finalMsg = nowTimeStr + " [" + threadName + "] " + levelStr + " " + name + " - " + msg + System.lineSeparator();
        if (throwable != null) {
            finalMsg = finalMsg + ExceptionUtils.getCustomStackTrace(throwable);
        }
        if (scxLoggerInfo.type == ScxLoggerInfoType.CONSOLE || scxLoggerInfo.type == ScxLoggerInfoType.BOTH) {
            System.out.print(finalMsg);
        }
        if (scxLoggerInfo.type == ScxLoggerInfoType.FILE || scxLoggerInfo.type == ScxLoggerInfoType.BOTH) {
            var filePath = Path.of(scxLoggerInfo.filePath.getPath(), nowTimeStr.split(" ")[0] + ".log");
            FileUtils.fileAppend(filePath, finalMsg.getBytes(StandardCharsets.UTF_8));
        }
    }

}
