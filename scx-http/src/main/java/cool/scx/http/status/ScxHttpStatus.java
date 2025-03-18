package cool.scx.http.status;

/// ScxHttpStatus
///
/// @author scx567888
/// @version 0.0.1
public sealed interface ScxHttpStatus permits HttpStatus, ScxHttpStatusImpl {

    static ScxHttpStatus of(int v) {
        // 优先使用 HttpStatus
        var m = HttpStatus.find(v);
        return m != null ? m : new ScxHttpStatusImpl(v);
    }

    /// 状态码
    int code();

    /// 状态码描述
    String description();

}
