package cool.scx.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static cool.scx.logging.ScxLoggerHelper.getTimeStamp;
import static cool.scx.logging.ScxLoggerHelper.isLoggerClass;
import static java.nio.file.StandardOpenOption.*;

/**
 * <p>ScxLoggerMessage class.</p>
 *
 * @author scx567888
 * @version 0.0.1
 */
final class ScxLoggerMessage extends StringWriter {

    private final String logFileName;

    /**
     * <p>Constructor for ScxLoggerMessage.</p>
     *
     * @param now   a {@link java.time.LocalDateTime} object
     * @param level a {@link cool.scx.logging.ScxLoggingLevel} object
     * @param name  a {@link java.lang.String} object
     * @param msg   a {@link java.lang.String} object
     */
    public ScxLoggerMessage(LocalDateTime now, ScxLoggingLevel level, String name, String msg) {
        var nowTimeStr = getTimeStamp(now);
        var currentThreadName = Thread.currentThread().getName();
        var levelStr = level.toFixedLengthString();

        this.logFileName = nowTimeStr.substring(0, 10) + ".log";

        // 创建初始的 message 格式如下
        // 时间戳                    线程名称  日志级别 日志名称                       具体内容
        // 2020-01-01 11:19:55.356 [main-1] ERROR cool.scx.xxx.TestController - 日志消息 !!!
        this.append(nowTimeStr)
                .append(" [").append(currentThreadName).append("] ")
                .append(levelStr)
                .append(" ").append(name).append(" - ")
                .append(msg).append(System.lineSeparator());
    }

    /**
     * a
     *
     * @param throwable a
     */
    public void appendThrowable(Throwable throwable) {
        throwable.printStackTrace(new PrintWriter(this));
    }

    /**
     * a
     *
     * @param e a
     */
    public void appendStackTraceInfo(Exception e) {
        var trace = e.getStackTrace();
        for (var traceElement : trace) {
            if (isLoggerClass(traceElement.getClassName())) {
                this.append("\t").append(traceElement.toString()).append(System.lineSeparator());
            }
        }
    }

    /**
     * 向文件写入
     *
     * @param logStoredPath a
     */
    public void writeToFile(Path logStoredPath) {
        if (logStoredPath == null) {
            return;
        }
        try {
            var path = Path.of(logStoredPath.toString(), this.logFileName);
            Files.createDirectories(path.getParent());
            Files.writeString(path, this.getBuffer().toString(), APPEND, CREATE, SYNC, WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * a
     *
     * @param level a
     */
    public void writeToConsole(ScxLoggingLevel level) {
        if (level.toInt() <= ScxLoggingLevel.ERROR.toInt()) {
            System.err.print(this.getBuffer().toString());
        } else {
            System.out.print(this.getBuffer().toString());
        }
    }

}
