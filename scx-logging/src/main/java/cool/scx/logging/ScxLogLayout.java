package cool.scx.logging;

/**
 * ScxLogLayout
 */
public interface ScxLogLayout {

    /**
     * 将 ScxLoggerEvent 格式化成字符串
     */
    String format(ScxLogEvent event);

}
