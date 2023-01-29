package cool.scx.logging;

/**
 * ScxLogEventFormatter
 *
 * @author scx567888
 * @version 2.0.8
 */
public interface ScxLogRecordFormatter {

    /**
     * 将 ScxLoggerEvent 格式化成字符串
     *
     * @param event a {@link cool.scx.logging.ScxLogRecord} object
     * @return a {@link java.lang.String} object
     */
    String format(ScxLogRecord event);

}
