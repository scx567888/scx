package cool.scx.http;

/**
 * HTTP Method 本质上是区分大小写的 但这里我们全部按照大写处理
 */
public sealed interface ScxHttpMethod permits HttpMethod, ScxHttpMethodImpl {

    static ScxHttpMethod of(String httpMethod) {
        try {
            // 优先使用 HttpMethod
            return HttpMethod.of(httpMethod);
        } catch (IllegalArgumentException e) {
            return new ScxHttpMethodImpl(httpMethod.toUpperCase());
        }
    }

    String value();

}
