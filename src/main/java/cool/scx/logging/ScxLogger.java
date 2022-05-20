package cool.scx.logging;

import cool.scx.util.exception.ScxExceptionHelper;
import cool.scx.util.file.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static java.nio.file.StandardOpenOption.*;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
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
    public ScxLogger(String name) {
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
     * a
     *
     * @param level     a
     * @param msg       a
     * @param throwable a
     */
    public void logMessage(ScxLoggingLevel level, String msg, Throwable throwable) {
        //关闭日志
        if (level == ScxLoggingLevel.OFF) {
            return;
        }
        var nowTimeStr = ScxLoggerConfiguration.LOG_DATETIME_FORMATTER.format(LocalDateTime.now());
        var stringBuilder = new StringBuilder();
        stringBuilder.append(nowTimeStr)
                .append(" [").append(Thread.currentThread().getName()).append("] ")
                .append(level.toFixedLengthString()).append(" ").append(name).append(" - ").append(msg)
                .append(System.lineSeparator());
        if (throwable != null) {
            stringBuilder.append(ScxExceptionHelper.getStackTraceString(throwable));
        } else if (stackTrace()) {
            ScxLoggerConfiguration.getStackTraceInfo(stringBuilder);
        }
        var finalMessage = stringBuilder.toString();
        if (type() == ScxLoggingType.CONSOLE || type() == ScxLoggingType.BOTH) {
            if (level.toInt() <= ScxLoggingLevel.ERROR.toInt()) {
                System.err.print(finalMessage);
            } else {
                System.out.print(finalMessage);
            }
        }
        if (type() == ScxLoggingType.FILE || type() == ScxLoggingType.BOTH) {
            var logStoredPath = storedDirectory().resolve(nowTimeStr.substring(0, 10) + ".log");
            try {
                FileUtils.write(logStoredPath, finalMessage.getBytes(StandardCharsets.UTF_8), APPEND, CREATE, SYNC, WRITE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <p>update.</p>
     *
     * @param newLevel           a {@link cool.scx.logging.ScxLoggingLevel} object
     * @param newType            a {@link cool.scx.logging.ScxLoggingType} object
     * @param newStoredDirectory a {@link java.nio.file.Path} object
     * @param newStackTrace      a {@link java.lang.Boolean} object
     */
    void update(ScxLoggingLevel newLevel, ScxLoggingType newType, Path newStoredDirectory, Boolean newStackTrace) {
        this.level = newLevel;
        this.type = newType;
        this.storedDirectory = newStoredDirectory;
        this.stackTrace = newStackTrace;
    }

}
