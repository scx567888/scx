package cool.scx.log;

import cool.scx.util.FileUtils;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.LegacyAbstractLogger;
import org.slf4j.helpers.MessageFormatter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScxLogger extends LegacyAbstractLogger {

    public static final DateTimeFormatter LOG_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private final ScxLoggerInfo scxLoggerInfo;

    public ScxLogger(String name) {
        this.name = name;
        this.scxLoggerInfo = ScxLogConfiguration.getLoggerInfo(name);
    }

    @Override
    protected String getFullyQualifiedCallerName() {
        return null;
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String message, Object[] arguments, Throwable throwable) {
        var nowTimeStr = LOG_DATETIME_FORMATTER.format(LocalDateTime.now());
        var threadName = Thread.currentThread().getName();
        var levelStr = (level == Level.INFO || level == Level.WARN) ? level + " " : level.toString();
        var msg = MessageFormatter.arrayFormat(message, arguments, throwable).getMessage();
        var finalMsg = nowTimeStr + " [" + threadName + "] " + levelStr + " " + name + " - " + msg;
        if (scxLoggerInfo.type == ScxLoggerInfoType.CONSOLE || scxLoggerInfo.type == ScxLoggerInfoType.BOTH) {
            System.out.println(finalMsg);
        }
        if (scxLoggerInfo.type == ScxLoggerInfoType.FILE || scxLoggerInfo.type == ScxLoggerInfoType.BOTH) {
            var filePath = Path.of(scxLoggerInfo.filePath.getPath(), nowTimeStr.split(" ")[0] + ".log");
            FileUtils.fileAppend(filePath, (finalMsg + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public boolean isTraceEnabled() {
        return scxLoggerInfo.level.toInt() <= Level.TRACE.toInt();
    }

    @Override
    public boolean isDebugEnabled() {
        return scxLoggerInfo.level.toInt() <= Level.DEBUG.toInt();
    }

    @Override
    public boolean isInfoEnabled() {
        return scxLoggerInfo.level.toInt() <= Level.INFO.toInt();
    }

    @Override
    public boolean isWarnEnabled() {
        return scxLoggerInfo.level.toInt() <= Level.WARN.toInt();
    }

    @Override
    public boolean isErrorEnabled() {
        return scxLoggerInfo.level.toInt() <= Level.ERROR.toInt();
    }

}
