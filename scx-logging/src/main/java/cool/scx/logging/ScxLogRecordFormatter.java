package cool.scx.logging;

/**
 * ScxLogEventFormatter
 *
 * @author scx567888
 * @version 2.0.8
 */
public interface ScxLogRecordFormatter {

    /**
     * 将 ScxLogRecord 格式化成字符串
     *
     * @param logRecord a {@link cool.scx.logging.ScxLogRecord} object
     * @return a {@link java.lang.String} object
     */
    String format(ScxLogRecord logRecord);

}
