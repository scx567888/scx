package cool.scx.http;

/**
 * HttpMethod
 *
 * @author scx567888
 * @version 0.3.6
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9110#name-method-definitions">https://www.rfc-editor.org/rfc/rfc9110#name-method-definitions</a>
 */
public enum HttpMethod implements ScxHttpMethod {

    /**
     * CONNECT 方法建立一个到由目标资源标识的服务器的隧道。
     */
    CONNECT("CONNECT"),

    /**
     * DELETE 方法删除指定的资源。
     */
    DELETE("DELETE"),

    /**
     * GET 方法请求一个指定资源的表示形式，使用 GET 的请求应该只被用于获取数据。
     */
    GET("GET"),

    /**
     * HEAD 方法请求一个与 GET 请求的响应相同的响应，但没有响应体。
     */
    HEAD("HEAD"),

    /**
     * OPTIONS 方法用于描述目标资源的通信选项。
     */
    OPTIONS("OPTIONS"),

    /**
     * PATCH 方法用于对资源应用部分修改。
     */
    PATCH("PATCH"),

    /**
     * POST 方法用于将实体提交到指定的资源，通常导致在服务器上的状态变化或副作用。
     */
    POST("POST"),

    /**
     * PUT 方法用有效载荷请求替换目标资源的所有当前表示。
     */
    PUT("PUT"),

    /**
     * TRACE 方法沿着到目标资源的路径执行一个消息环回测试。
     */
    TRACE("TRACE");

    private final String value;

    HttpMethod(String value) {
        this.value = value;
    }

    public static HttpMethod of(String v) {
        return switch (v) {
            case "CONNECT" -> CONNECT;
            case "DELETE" -> DELETE;
            case "GET" -> GET;
            case "HEAD" -> HEAD;
            case "OPTIONS" -> OPTIONS;
            case "PATCH" -> PATCH;
            case "POST" -> POST;
            case "PUT" -> PUT;
            case "TRACE" -> TRACE;
            default -> throw new IllegalArgumentException("Unknown http method: " + v);
        };
    }

    @Override
    public String value() {
        return this.value;
    }

}
