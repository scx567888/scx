package cool.scx.http;

/**
 * HTTP Method (注意 需要 区分大小写)
 */
public sealed interface ScxHttpMethod permits HttpMethod, ScxHttpMethodImpl {

    static ScxHttpMethod of(String httpMethod) {
        try {
            return HttpMethod.of(httpMethod);
        } catch (IllegalArgumentException e) {
            return new ScxHttpMethodImpl(httpMethod);
        }
    }

    String value();

}
