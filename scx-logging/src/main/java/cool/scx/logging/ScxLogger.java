package cool.scx.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static cool.scx.logging.ScxLoggerHelper.*;
import static java.nio.file.StandardOpenOption.*;

/**
 * ScxLogger
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ScxLogger {

    /**
     * 日志名称
     */
    private final String name;

    /**
     * 日志级别
     */
    private ScxLoggingLevel level = null;

    /**
     * 日志类型
     */
    private ScxLoggingType type = null;

    /**
     * 存储目录
     */
    private Path storedDirectory = null;

    /**
     * 是否启用堆栈跟踪
     */
    private Boolean stackTrace = null;

    /**
     * 消息格式化器
     */
    private ScxLoggerMessageFormatter messageFormatter = null;

    /**
     * a
     *
     * @param name a
     */
    public ScxLogger(String name) {
        this.name = name;
    }

    /**
     * 打印日志方法
     *
     * @param level     a
     * @param msg       a
     * @param throwable a
     */
    public void logMessage(ScxLoggingLevel level, String msg, Throwable throwable) {

        //不需要打印直接返回
        if (dontNeedLog(level)) {
            return;
        }

        //当前时间
        var now = LocalDateTime.now();
        //堆栈跟踪对象
        var stackTraceInfo = stackTrace() ? getStackTraceInfo(new Exception()) : null;
        // 格式化 message
        var message = messageFormatter().format(now, level, this.name, msg, throwable, stackTraceInfo);

        var t = type();

        //向控制台写入
        if (needWriteToConsole(t)) {
            //错误级别的我们就采用 err 打印 其余的 正常输出
            if (level.toInt() <= ScxLoggingLevel.ERROR.toInt()) {
                System.err.print(message);
            } else {
                System.out.print(message);
            }
        }

        //向日志文件写入
        if (needWriteToFile(t)) {
            var directory = storedDirectory();
            if (directory == null) {
                return;
            }
            try {
                var logFileName = getLogFileName(now);
                var path = directory.resolve(logFileName);
                Files.createDirectories(path.getParent());
                Files.writeString(path, message, APPEND, CREATE, SYNC, WRITE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * <p>Setter for the field <code>level</code>.</p>
     *
     * @param newLevel a {@link cool.scx.logging.ScxLoggingLevel} object
     * @return a
     */
    public final ScxLogger setLevel(ScxLoggingLevel newLevel) {
        this.level = newLevel;
        return this;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param newType a {@link cool.scx.logging.ScxLoggingType} object
     * @return a
     */
    public final ScxLogger setType(ScxLoggingType newType) {
        this.type = newType;
        return this;
    }

    /**
     * <p>Setter for the field <code>storedDirectory</code>.</p>
     *
     * @param newStoredDirectory a {@link java.nio.file.Path} object
     * @return a
     */
    public final ScxLogger setStoredDirectory(Path newStoredDirectory) {
        this.storedDirectory = newStoredDirectory;
        return this;
    }

    /**
     * <p>Setter for the field <code>stackTrace</code>.</p>
     *
     * @param newStackTrace a {@link java.lang.Boolean} object
     * @return a
     */
    public final ScxLogger setStackTrace(Boolean newStackTrace) {
        this.stackTrace = newStackTrace;
        return this;
    }

    /**
     * a
     *
     * @param newMessageFormatter a
     * @return a
     */
    public final ScxLogger setMessageFormatter(ScxLoggerMessageFormatter newMessageFormatter) {
        this.messageFormatter = newMessageFormatter;
        return this;
    }

    /**
     * a
     *
     * @return a
     */
    public final ScxLoggingLevel level() {
        return level != null ? level : ScxLoggerFactory.defaultLevel();
    }

    /**
     * a
     *
     * @return a
     */
    public final ScxLoggingType type() {
        return type != null ? type : ScxLoggerFactory.defaultType();
    }

    /**
     * a
     *
     * @return a
     */
    public final Path storedDirectory() {
        return storedDirectory != null ? storedDirectory : ScxLoggerFactory.defaultStoredDirectory();
    }

    /**
     * a
     *
     * @return a
     */
    public final boolean stackTrace() {
        return stackTrace != null ? stackTrace : ScxLoggerFactory.defaultStackTrace();
    }

    /**
     * a
     *
     * @return a
     */
    public final ScxLoggerMessageFormatter messageFormatter() {
        return messageFormatter != null ? messageFormatter : ScxLoggerFactory.defaultMessageFormatter();
    }

}
