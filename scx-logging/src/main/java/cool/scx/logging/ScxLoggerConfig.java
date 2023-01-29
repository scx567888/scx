package cool.scx.logging;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>ScxLoggerConfig class.</p>
 *
 * @author scx567888
 * @version 2.0.8
 */
public class ScxLoggerConfig {

    private final ScxLoggerConfig parent;

    private ScxLoggingLevel level = null;

    private Boolean stackTrace = null;

    private Set<ScxLogRecorder> recorders = null;

    /**
     * <p>Constructor for ScxLoggerConfig.</p>
     *
     * @param parent a {@link cool.scx.logging.ScxLoggerConfig} object
     */
    public ScxLoggerConfig(ScxLoggerConfig parent) {
        this.parent = parent;
    }

    /**
     * <p>level.</p>
     *
     * @return a {@link cool.scx.logging.ScxLoggingLevel} object
     */
    public final ScxLoggingLevel level() {
        return level != null ? level : parent != null ? parent.level() : ScxLoggingLevel.ERROR;
    }

    /**
     * <p>stackTrace.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public final Boolean stackTrace() {
        return stackTrace != null ? stackTrace : parent != null ? parent.stackTrace() : false;
    }

    /**
     * <p>recorders.</p>
     *
     * @return a {@link java.util.Set} object
     */
    public final Set<ScxLogRecorder> recorders() {
        return recorders != null ? recorders : parent != null ? parent.recorders() : new HashSet<>();
    }

    /**
     * <p>Setter for the field <code>level</code>.</p>
     *
     * @param newLevel a {@link cool.scx.logging.ScxLoggingLevel} object
     * @return a {@link cool.scx.logging.ScxLoggerConfig} object
     */
    public final ScxLoggerConfig setLevel(ScxLoggingLevel newLevel) {
        this.level = newLevel;
        return this;
    }

    /**
     * <p>Setter for the field <code>stackTrace</code>.</p>
     *
     * @param newStackTrace a {@link java.lang.Boolean} object
     * @return a {@link cool.scx.logging.ScxLoggerConfig} object
     */
    public final ScxLoggerConfig setStackTrace(Boolean newStackTrace) {
        this.stackTrace = newStackTrace;
        return this;
    }

    /**
     * <p>addRecorder.</p>
     *
     * @param recorder  a {@link cool.scx.logging.ScxLogRecorder} object
     * @param recorders a {@link cool.scx.logging.ScxLogRecorder} object
     * @return a {@link cool.scx.logging.ScxLoggerConfig} object
     */
    public final ScxLoggerConfig addRecorder(ScxLogRecorder recorder, ScxLogRecorder... recorders) {
        if (this.recorders == null) {
            this.recorders = new HashSet<>();
        }
        this.recorders.add(recorder);
        this.recorders.addAll(List.of(recorders));
        return this;
    }

    /**
     * <p>removeRecorder.</p>
     *
     * @param recorder a {@link cool.scx.logging.ScxLogRecorder} object
     * @return a {@link cool.scx.logging.ScxLoggerConfig} object
     */
    public final ScxLoggerConfig removeRecorder(ScxLogRecorder recorder) {
        if (this.recorders != null) {
            this.recorders.remove(recorder);
        }
        return this;
    }

    /**
     * <p>clearRecorders.</p>
     *
     * @return a {@link cool.scx.logging.ScxLoggerConfig} object
     */
    public final ScxLoggerConfig clearRecorders() {
        this.recorders = null;
        return this;
    }

}
