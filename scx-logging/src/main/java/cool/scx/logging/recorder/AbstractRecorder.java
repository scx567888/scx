package cool.scx.logging.recorder;

import cool.scx.logging.ScxLogRecord;
import cool.scx.logging.ScxLogRecorder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static java.lang.System.Logger.Level;

public abstract class AbstractRecorder implements ScxLogRecorder {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    protected Level level = null;

    public Level level() {
        return level;
    }

    public AbstractRecorder setLevel(Level newLevel) {
        this.level = newLevel;
        return this;
    }

    public String formatTimeStamp(TemporalAccessor temporal) {
        return DATE_TIME_FORMATTER.format(temporal);
    }

    public String formatStackTrace(StackTraceElement[] stackTraces) {
        var sb = new StringBuilder();
        for (var traceElement : stackTraces) {
            sb.append("\t").append(traceElement).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public String formatLevel(Level level) {
        return switch (level) {
            case ALL -> "ALL  ";
            case TRACE -> "TRACE";
            case DEBUG -> "DEBUG";
            case INFO -> "INFO ";
            case WARNING -> "WARN ";
            case ERROR -> "ERROR";
            case OFF -> "OFF  ";
        };
    }

    public String format(ScxLogRecord event) {
        // 创建初始的 message 格式如下
        // 时间戳                    线程名称  日志级别 日志名称                       具体内容
        // 2020-01-01 11:19:55.356 [main-1] ERROR cool.scx.xxx.TestController - 日志消息 !!!
        var sw = new StringWriter().append(formatTimeStamp(event.timeStamp()))
                .append(" [").append(event.threadName()).append("] ")
                .append(formatLevel(event.level()))
                .append(" ").append(event.loggerName()).append(" - ")
                .append(event.message()).append(System.lineSeparator());

        // throwable 和 stackTraceInfo 没必要同时出现
        if (event.throwable() != null) {
            event.throwable().printStackTrace(new PrintWriter(sw));
        } else if (event.contextStack() != null) {
            sw.append(formatStackTrace(event.contextStack()));
        }
        return sw.toString();
    }

    @Override
    public final void record(ScxLogRecord logRecord) {
        if (isRecordable(logRecord)) {
            record0(logRecord);
        }
    }

    public boolean isRecordable(ScxLogRecord logRecord) {
        return level == null || level.getSeverity() <= logRecord.level().getSeverity();
    }

    public abstract void record0(ScxLogRecord logRecord);

}
