package cool.scx.http.status;

/// ScxHttpStatus
///
/// @author scx567888
/// @version 0.0.1
public sealed interface ScxHttpStatus permits HttpStatus, ScxHttpStatusImpl {

    static ScxHttpStatus of(int code) {
        // 优先使用 HttpStatus
        var m = HttpStatus.find(code);
        return m != null ? m : new ScxHttpStatusImpl(code);
    }

    /// 创建自定义的 状态码 支持 自定义描述
    static ScxHttpStatus of(int code, String description) {
        return new ScxHttpStatusImpl(code, description);
    }

    /// 状态码
    int code();

    /// 状态码描述
    String description();

    /// 是否相等
    default boolean equals(ScxHttpStatus other) {
        return this.code() == other.code();
    }

}
