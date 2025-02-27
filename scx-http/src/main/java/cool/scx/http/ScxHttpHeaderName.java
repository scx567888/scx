package cool.scx.http;

/// HttpHeaderName 是不区分大小写的 所以我们这里全部按照小写处理
///
/// @author scx567888
/// @version 0.0.1
public sealed interface ScxHttpHeaderName permits HttpFieldName, ScxHttpHeaderNameImpl {

    static ScxHttpHeaderName of(String name) {
        // 优先使用 HttpFieldName
        var n = HttpFieldName.find(name);
        return n != null ? n : new ScxHttpHeaderNameImpl(name.toLowerCase());
    }

    String value();

}
