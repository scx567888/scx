package cool.scx.logging;

import cool.scx.logging.formatter.DefaultFormatter;

/**
 * 日志记录器
 */
public abstract class ScxLogRecorder {

    private ScxLogEventFormatter formatter;

    public abstract void record(ScxLogEvent event);

    public abstract String name();

    public ScxLogRecorder setFormatter(ScxLogEventFormatter formatter) {
        this.formatter = formatter;
        return this;
    }

    public ScxLogEventFormatter formatter() {
        return formatter != null ? formatter : DefaultFormatter.DEFAULT_INSTANCE;
    }

}
