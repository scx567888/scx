package cool.scx.logging;

import cool.scx.util.FileUtils;
import cool.scx.util.exception.ScxExceptionHelper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * a
 */
public final class ScxLogger {

    private final String name;
    private ScxLoggingLevel level = null;
    private ScxLoggingType type = null;
    private Path storedDirectory = null;

    /**
     * a
     *
     * @param name a
     */
    public ScxLogger(String name) {
        this.name = name;
    }

    void level(ScxLoggingLevel newLevel) {
        Objects.requireNonNull(newLevel);
        this.level = newLevel;
    }

    void type(ScxLoggingType newType) {
        Objects.requireNonNull(newType);
        this.type = newType;
    }

    void storedDirectory(Path newStoredDirectory) {
        Objects.requireNonNull(newStoredDirectory);
        this.storedDirectory = newStoredDirectory;
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
    public ScxLoggingType type() {
        return type != null ? type : ScxLoggerFactory.defaultType();
    }

    /**
     * a
     *
     * @return a
     */
    public Path storedDirectory() {
        return storedDirectory != null ? storedDirectory : ScxLoggerFactory.defaultStoredDirectory();
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
            stringBuilder.append(ScxExceptionHelper.getCustomStackTrace(throwable));
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
            FileUtils.fileAppend(logStoredPath, finalMessage.getBytes(StandardCharsets.UTF_8));
        }
    }

}
