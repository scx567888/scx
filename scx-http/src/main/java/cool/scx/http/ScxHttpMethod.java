package cool.scx.http;

/// HTTP Method (注意 需要 区分大小写)
///
/// @author scx567888
/// @version 0.0.1
public sealed interface ScxHttpMethod permits HttpMethod, ScxHttpMethodImpl {

    static ScxHttpMethod of(String v) {
        // 优先使用 HttpMethod
        var m = HttpMethod.find(v);
        return m != null ? m : new ScxHttpMethodImpl(v);
    }

    String value();

}
