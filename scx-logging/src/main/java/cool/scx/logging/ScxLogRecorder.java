package cool.scx.logging;

import cool.scx.logging.formatter.DefaultFormatter;

/**
 * 日志记录器
 */
public abstract class ScxLogRecorder {

    private ScxLogRecordFormatter formatter;

    public abstract void record(ScxLogRecord logRecord);

    public abstract String name();

    public ScxLogRecorder setFormatter(ScxLogRecordFormatter formatter) {
        this.formatter = formatter;
        return this;
    }

    public ScxLogRecordFormatter formatter() {
        return formatter != null ? formatter : DefaultFormatter.DEFAULT_INSTANCE;
    }

}
