package cool.scx.logging;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScxLoggerConfig {

    private final ScxLoggerConfig parent;

    private ScxLoggingLevel level = null;

    private Boolean stackTrace = null;

    private Set<ScxLogRecorder> recorders = null;

    public ScxLoggerConfig(ScxLoggerConfig parent) {
        this.parent = parent;
    }

    public final ScxLoggingLevel level() {
        return level != null ? level : parent != null ? parent.level() : ScxLoggingLevel.ERROR;
    }

    public final Boolean stackTrace() {
        return stackTrace != null ? stackTrace : parent != null ? parent.stackTrace() : false;
    }

    public final Set<ScxLogRecorder> recorders() {
        return recorders != null ? recorders : parent != null ? parent.recorders() : new HashSet<>();
    }

    public final ScxLoggerConfig setLevel(ScxLoggingLevel newLevel) {
        this.level = newLevel;
        return this;
    }

    public final ScxLoggerConfig setStackTrace(Boolean newStackTrace) {
        this.stackTrace = newStackTrace;
        return this;
    }

    public final ScxLoggerConfig addRecorder(ScxLogRecorder recorder, ScxLogRecorder... recorders) {
        if (this.recorders == null) {
            this.recorders = new HashSet<>();
        }
        this.recorders.add(recorder);
        this.recorders.addAll(List.of(recorders));
        return this;
    }

    public final ScxLoggerConfig removeRecorder(ScxLogRecorder recorder) {
        if (this.recorders != null) {
            this.recorders.remove(recorder);
        }
        return this;
    }

    public final ScxLoggerConfig clearRecorders() {
        this.recorders = null;
        return this;
    }

}
