package cool.scx.enumeration;

/**
 * <p>HttpMethod class.</p>
 *
 * @author scx567888
 * @version 0.3.6
 */
public enum HttpMethod {

    /**
     * GET 方法请求一个指定资源的表示形式，使用 GET 的请求应该只被用于获取数据。
     */
    GET,

    /**
     * HEAD 方法请求一个与 GET 请求的响应相同的响应，但没有响应体。
     */
    HEAD,

    /**
     * POST 方法用于将实体提交到指定的资源，通常导致在服务器上的状态变化或副作用。
     */
    POST,

    /**
     * PUT 方法用有效载荷请求替换目标资源的所有当前表示。
     */
    PUT,

    /**
     * DELETE 方法删除指定的资源。
     */
    DELETE,

    /**
     * CONNECT 方法建立一个到由目标资源标识的服务器的隧道。
     */
    CONNECT,

    /**
     * OPTIONS 方法用于描述目标资源的通信选项。
     */
    OPTIONS,

    /**
     * TRACE 方法沿着到目标资源的路径执行一个消息环回测试。
     */
    TRACE,

    /**
     * PATCH 方法用于对资源应用部分修改。
     */
    PATCH;

    private final io.vertx.core.http.HttpMethod vertxMethod;

    HttpMethod() {
        this.vertxMethod = io.vertx.core.http.HttpMethod.valueOf(this.name());
    }

    /**
     * 对应的 vertx 的 HttpMethod, 方便在 vertx 应用中直接使用 .
     *
     * @return a {@link io.vertx.core.http.HttpMethod} object
     */
    public io.vertx.core.http.HttpMethod vertxMethod() {
        return this.vertxMethod;
    }

}
