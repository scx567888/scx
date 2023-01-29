package cool.scx.logging.formatter;

import cool.scx.logging.ScxLogEvent;
import cool.scx.logging.ScxLogEventFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * 默认格式化器
 *
 * @author scx567888
 * @version 2.0.8
 */
public final class DefaultFormatter implements ScxLogEventFormatter {

    /**
     * Constant <code>DEFAULT_INSTANCE</code>
     */
    public static final ScxLogEventFormatter DEFAULT_INSTANCE = new DefaultFormatter();

    /**
     * 默认格式化时间的类型
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * <p>getTimeStamp.</p>
     *
     * @param temporal a {@link java.time.LocalDateTime} object
     * @return a {@link java.lang.String} object
     */
    public static String formatTimeStamp(TemporalAccessor temporal) {
        return DATE_TIME_FORMATTER.format(temporal);
    }

    /**
     * 获取过滤后的堆栈信息 (字符串形式)
     *
     * @param stackTraces a
     * @return a {@link java.lang.String} object
     */
    public static String formatStackTrace(StackTraceElement[] stackTraces) {
        var sb = new StringBuilder();
        for (var traceElement : stackTraces) {
            sb.append("\t").append(traceElement).append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String format(ScxLogEvent event) {
        // 创建初始的 message 格式如下
        // 时间戳                    线程名称  日志级别 日志名称                       具体内容
        // 2020-01-01 11:19:55.356 [main-1] ERROR cool.scx.xxx.TestController - 日志消息 !!!
        var sw = new StringWriter().append(formatTimeStamp(event.timeStamp()))
                .append(" [").append(event.threadName()).append("] ")
                .append(event.level().fixedLengthName())
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

}
