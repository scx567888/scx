package cool.scx.http;

/**
 * HTTP Method (注意 需要 区分大小写)
 */
public sealed interface ScxHttpMethod permits HttpMethod, ScxHttpMethodImpl {

    static ScxHttpMethod of(String v) {
        // 优先使用 HttpMethod
        var m= HttpMethod.of(v);
        return m != null ? m : new ScxHttpMethodImpl(v);
    }

    String value();

}
