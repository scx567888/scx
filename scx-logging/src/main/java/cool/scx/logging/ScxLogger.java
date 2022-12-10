package cool.scx.logging;

import java.nio.file.Path;
import java.time.LocalDateTime;

import static cool.scx.logging.ScxLoggerHelper.*;

/**
 * ScxLogger
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxLogger {

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
     * a
     *
     * @param name a
     */
    ScxLogger(String name) {
        this.name = name;
    }

    /**
     * a
     *
     * @return a
     */
    public ScxLoggingLevel level() {
        return level != null ? level : ScxLoggerFactory.defaultLevel();
    }

    /**
     * a
     *
     * @return a
     */
    private ScxLoggingType type() {
        return type != null ? type : ScxLoggerFactory.defaultType();
    }

    /**
     * a
     *
     * @return a
     */
    private Path storedDirectory() {
        return storedDirectory != null ? storedDirectory : ScxLoggerFactory.defaultStoredDirectory();
    }

    /**
     * a
     *
     * @return a
     */
    private boolean stackTrace() {
        return stackTrace != null ? stackTrace : ScxLoggerFactory.defaultStackTrace();
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

        // 创建初始的 message 对象
        var message = new ScxLoggerMessage(LocalDateTime.now(), level, this.name, msg);

        //如果有错误则添加错误
        if (throwable != null) {
            message.appendThrowable(throwable);
        } else if (stackTrace()) {
            //没有错误但开启了 堆栈记录则记录堆栈信息
            message.appendStackTraceInfo(new Exception());
        }

        var t = type();

        //向控制台写入
        if (needWriteToConsole(t)) {
            message.writeToConsole(level);
        }

        //向日志文件写入
        if (needWriteToFile(t)) {
            message.writeToFile(storedDirectory());
        }

    }

    /**
     * <p>Setter for the field <code>level</code>.</p>
     *
     * @param newLevel a {@link cool.scx.logging.ScxLoggingLevel} object
     * @return a
     */
    ScxLogger setLevel(ScxLoggingLevel newLevel) {
        this.level = newLevel;
        return this;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param newType a {@link cool.scx.logging.ScxLoggingType} object
     * @return a
     */
    ScxLogger setType(ScxLoggingType newType) {
        this.type = newType;
        return this;
    }

    /**
     * <p>Setter for the field <code>storedDirectory</code>.</p>
     *
     * @param newStoredDirectory a {@link java.nio.file.Path} object
     * @return a
     */
    ScxLogger setStoredDirectory(Path newStoredDirectory) {
        this.storedDirectory = newStoredDirectory;
        return this;
    }

    /**
     * <p>Setter for the field <code>stackTrace</code>.</p>
     *
     * @param newStackTrace a {@link java.lang.Boolean} object
     * @return a
     */
    ScxLogger setStackTrace(Boolean newStackTrace) {
        this.stackTrace = newStackTrace;
        return this;
    }

}
