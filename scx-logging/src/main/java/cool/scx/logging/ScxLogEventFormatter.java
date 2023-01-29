package cool.scx.logging;

/**
 * ScxLogEventFormatter
 *
 * @author scx567888
 * @version 2.0.8
 */
public interface ScxLogEventFormatter {

    /**
     * 将 ScxLoggerEvent 格式化成字符串
     *
     * @param event a {@link cool.scx.logging.ScxLogEvent} object
     * @return a {@link java.lang.String} object
     */
    String format(ScxLogEvent event);

}
