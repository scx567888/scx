package cool.scx.logging.layout;

import cool.scx.logging.ScxLogEvent;
import cool.scx.logging.ScxLogLayout;
import cool.scx.logging.ScxLoggerHelper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 默认格式化器
 */
public final class DefaultLayout implements ScxLogLayout {

    /**
     * 默认格式化时间的类型
     */
    private static final DateTimeFormatter LOG_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * <p>getTimeStamp.</p>
     *
     * @param time a {@link java.time.LocalDateTime} object
     * @return a {@link java.lang.String} object
     */
    public static String formatTimeStamp(LocalDateTime time) {
        return LOG_DATE_TIME.format(time);
    }

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
            sw.append(ScxLoggerHelper.formatStackTrace(event.contextStack()));
        }
        return sw.toString();
    }

    public static final ScxLogLayout DEFAULT_LAYOUT = new DefaultLayout();

}
