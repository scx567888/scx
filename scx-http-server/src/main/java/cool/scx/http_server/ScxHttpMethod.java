package cool.scx.http_server;

public sealed interface ScxHttpMethod permits HttpMethod, ScxHttpMethodImpl {

    static ScxHttpMethod of(String httpMethod) {
        try {
            // 优先使用 HttpMethod
            return HttpMethod.valueOf(httpMethod.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ScxHttpMethodImpl(httpMethod);
        }
    }

    String value();

}
