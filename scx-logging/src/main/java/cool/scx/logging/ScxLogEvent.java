package cool.scx.logging;

import java.time.LocalDateTime;

/**
 * ScxLog 记录事件
 *
 * @param timeStamp    时间戳(日志触发的时间)
 * @param level        日志级别
 * @param loggerName   日志名称
 * @param message      消息
 * @param threadName   线程名称
 * @param throwable    错误信息 (可能为 null)
 * @param contextStack 上下文堆栈跟踪信息 (可能为 null)
 */
public record ScxLogEvent(LocalDateTime timeStamp,
                          ScxLoggingLevel level,
                          String loggerName,
                          String message,
                          String threadName,
                          Throwable throwable,
                          StackTraceElement[] contextStack) {

}
