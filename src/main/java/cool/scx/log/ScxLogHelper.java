package cool.scx.log;

import cool.scx.util.ExceptionUtils;
import cool.scx.util.FileUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * a
 */
public final class ScxLogHelper {

    private static final DateTimeFormatter LOG_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * a
     *
     * @param name          a
     * @param level         a
     * @param msg           a
     * @param scxLoggerInfo a
     * @param throwable     a
     */
    public static void logMessage(String name, ScxLogLevel level, String msg, ScxLoggerInfo scxLoggerInfo, Throwable throwable) {
        //关闭日志
        if (level == ScxLogLevel.OFF) {
            return;
        }
        var nowTimeStr = ScxLogHelper.LOG_DATETIME_FORMATTER.format(LocalDateTime.now());
        var stringBuilder = new StringBuilder();
        stringBuilder.append(nowTimeStr)
                .append(" [").append(Thread.currentThread().getName()).append("] ")
                .append(level.toFixedLengthString()).append(" ").append(name).append(" - ").append(msg)
                .append(System.lineSeparator());
        if (throwable != null) {
            stringBuilder.append(ExceptionUtils.getCustomStackTrace(throwable));
        }
        var finalMessage = stringBuilder.toString();
        if (scxLoggerInfo.loggingType() == ScxLogLoggingType.CONSOLE || scxLoggerInfo.loggingType() == ScxLogLoggingType.BOTH) {
            if (level.toInt() <= ScxLogLevel.ERROR.toInt()) {
                System.err.print(finalMessage);
            } else {
                System.out.print(finalMessage);
            }
        }
        if (scxLoggerInfo.loggingType() == ScxLogLoggingType.FILE || scxLoggerInfo.loggingType() == ScxLogLoggingType.BOTH) {
            var logStoredPath = scxLoggerInfo.storedDirectory().resolve(nowTimeStr.substring(0, 10) + ".log");
            FileUtils.fileAppend(logStoredPath, finalMessage.getBytes(StandardCharsets.UTF_8));
        }
    }

}
