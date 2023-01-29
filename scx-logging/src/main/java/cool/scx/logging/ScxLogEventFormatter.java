package cool.scx.logging;

/**
 * ScxLogEventFormatter
 */
public interface ScxLogEventFormatter {

    /**
     * 将 ScxLoggerEvent 格式化成字符串
     */
    String format(ScxLogEvent event);

}
