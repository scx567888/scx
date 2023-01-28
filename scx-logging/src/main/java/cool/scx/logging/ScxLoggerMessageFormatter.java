package cool.scx.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

import static cool.scx.logging.ScxLoggerHelper.getTimeStamp;

public interface ScxLoggerMessageFormatter {

    /**
     * 将参数格式化位 字符串以便展示和打印
     *
     * @param localDateTime  日志触发的时间
     * @param level          日志级别
     * @param name           日志名称
     * @param message        消息
     * @param throwable      错误信息 (可能为 null)
     * @param stackTraceInfo 堆栈跟踪信息 (可能为 null)
     * @return 字符串 注意需要自行设置结尾的换行
     */
    String format(LocalDateTime localDateTime, ScxLoggingLevel level, String name, String message, Throwable throwable, String stackTraceInfo);

    /**
     * 默认的 消息格式化器
     */
    ScxLoggerMessageFormatter DEFAULT_SCX_LOGGER_MESSAGE_FORMATTER = (now, level, name, message, throwable, stackTraceInfo) -> {
        var nowTimeStr = getTimeStamp(now);
        var currentThreadName = Thread.currentThread().getName();
        var levelStr = level.fixedLengthName();

        var sw = new StringWriter();
        // 创建初始的 message 格式如下
        // 时间戳                    线程名称  日志级别 日志名称                       具体内容
        // 2020-01-01 11:19:55.356 [main-1] ERROR cool.scx.xxx.TestController - 日志消息 !!!
        sw.append(nowTimeStr)
                .append(" [").append(currentThreadName).append("] ")
                .append(levelStr)
                .append(" ").append(name).append(" - ")
                .append(message).append(System.lineSeparator());

        // throwable 和 stackTraceInfo 没必要同时出现
        if (throwable != null) {
            throwable.printStackTrace(new PrintWriter(sw));
        } else if (stackTraceInfo != null) {
            sw.append(stackTraceInfo);
        }
        return sw.toString();
    };

}
