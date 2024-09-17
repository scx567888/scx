package cool.scx.http;

/**
 * HttpHeaderName 是不区分大小写的 所以我们这里全部按照小写处理
 */
public sealed interface ScxHttpHeaderName permits HttpFieldName, ScxHttpHeaderNameImpl {

    static ScxHttpHeaderName of(String name) {
        try {
            // 优先使用 HttpFieldName
            return HttpFieldName.of(name);
        } catch (IllegalArgumentException e) {
            return new ScxHttpHeaderNameImpl(name.toLowerCase());
        }
    }

    String value();

}
