package cool.scx.logging;

import java.util.HashSet;
import java.util.Set;

import static java.lang.System.Logger.Level;
import static java.util.Collections.addAll;

/// ScxLoggerConfig
///
/// @author scx567888
/// @version 0.0.1
public final class ScxLoggerConfig {

    private final ScxLoggerConfig parent;

    private Level level = null;
    private Boolean stackTrace = null;
    private Set<ScxLogRecorder> recorders = null;

    public ScxLoggerConfig(ScxLoggerConfig parent) {
        this.parent = parent;
    }

    public ScxLoggerConfig() {
        this.parent = null;
    }

    public Level level() {
        if (level != null) {
            return level;
        }
        if (parent != null) {
            return parent.level();
        }
        throw new IllegalStateException("level is not configured and no fallback found");
    }

    public Boolean stackTrace() {
        if (stackTrace != null) {
            return stackTrace;
        }
        if (parent != null) {
            return parent.stackTrace();
        }
        throw new IllegalStateException("stackTrace is not configured and no fallback found");
    }

    public Set<ScxLogRecorder> recorders() {
        if (recorders != null) {
            return recorders;
        }
        if (parent != null) {
            return parent.recorders();
        }
        throw new IllegalStateException("recorders is not configured and no fallback found");
    }

    public ScxLoggerConfig setLevel(Level newLevel) {
        this.level = newLevel;
        return this;
    }

    public ScxLoggerConfig setStackTrace(Boolean newStackTrace) {
        this.stackTrace = newStackTrace;
        return this;
    }

    public ScxLoggerConfig setRecorder(Set<ScxLogRecorder> recorders) {
        this.recorders = recorders != null ? new HashSet<>(recorders) : null;
        return this;
    }

    public ScxLoggerConfig clearLevel() {
        this.level = null;
        return this;
    }

    public ScxLoggerConfig clearStackTrace() {
        this.stackTrace = null;
        return this;
    }

    public ScxLoggerConfig clearRecorders() {
        this.recorders = null;
        return this;
    }

    public ScxLoggerConfig addRecorder(ScxLogRecorder... recorders) {
        if (this.recorders == null) {
            this.recorders = new HashSet<>();
        }
        addAll(this.recorders, recorders);
        return this;
    }

    public ScxLoggerConfig removeRecorder(ScxLogRecorder recorder) {
        if (this.recorders != null) {
            this.recorders.remove(recorder);
        }
        return this;
    }

    /// 根据其他配置更新当前配置
    ///
    /// @param newConfig 新的配置对象
    /// @return this
    public ScxLoggerConfig updateConfig(ScxLoggerConfig newConfig) {
        setLevel(newConfig.level);
        setStackTrace(newConfig.stackTrace);
        setRecorder(newConfig.recorders);
        return this;
    }

}
