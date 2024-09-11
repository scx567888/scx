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
    CONNECT,

    /**
     * DELETE 方法删除指定的资源。
     */
    DELETE,

    /**
     * GET 方法请求一个指定资源的表示形式，使用 GET 的请求应该只被用于获取数据。
     */
    GET,

    /**
     * HEAD 方法请求一个与 GET 请求的响应相同的响应，但没有响应体。
     */
    HEAD,

    /**
     * OPTIONS 方法用于描述目标资源的通信选项。
     */
    OPTIONS,

    /**
     * PATCH 方法用于对资源应用部分修改。
     */
    PATCH,

    /**
     * POST 方法用于将实体提交到指定的资源，通常导致在服务器上的状态变化或副作用。
     */
    POST,

    /**
     * PUT 方法用有效载荷请求替换目标资源的所有当前表示。
     */
    PUT,

    /**
     * TRACE 方法沿着到目标资源的路径执行一个消息环回测试。
     */
    TRACE;

    public static HttpMethod of(String v) {
        var upperCase = v.toUpperCase();
        return valueOf(upperCase);
    }

    @Override
    public String value() {
        return this.name();
    }

}
