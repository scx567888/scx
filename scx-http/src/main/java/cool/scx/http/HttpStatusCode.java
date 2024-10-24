package cool.scx.http;

/**
 * HttpStatusCode
 *
 * @author scx567888
 * @version 1.11.8
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9110#name-status-codes">https://www.rfc-editor.org/rfc/rfc9110#name-status-codes</a>
 */
public enum HttpStatusCode {

    /**
     * 这个临时响应表明，迄今为止的所有内容都是可行的，客户端应该继续请求，如果已经完成，则忽略它。
     */
    CONTINUE(100, "Continue"),

    /**
     * 该代码是响应客户端的 Upgrade (en-US) 请求头发送的，指明服务器即将切换的协议。
     */
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),

    /**
     * 请求成功。成功的含义取决于 HTTP 方法：
     * <p>
     * GET: 资源已被提取并在消息正文中传输。
     * HEAD: 实体标头位于消息正文中。
     * PUT or POST: 描述动作结果的资源在消息体中传输。
     * TRACE: 消息正文包含服务器收到的请求消息。
     */
    OK(200, "OK"),

    /**
     * 该请求已成功，并因此创建了一个新的资源。这通常是在 POST 请求，或是某些 PUT 请求之后返回的响应。
     */
    CREATED(201, "Created"),

    /**
     * 请求已经接收到，但还未响应，没有结果。意味着不会有一个异步的响应去表明当前请求的结果，预期另外的进程和服务去处理请求，或者批处理。
     */
    ACCEPTED(202, "Accepted"),

    /**
     * 服务器已成功处理了请求，但返回的实体头部元信息不是在原始服务器上有效的确定集合，而是来自本地或者第三方的拷贝。当前的信息可能是原始版本的子集或者超集。例如，包含资源的元数据可能导致原始服务器知道元信息的超集。使用此状态码不是必须的，而且只有在响应不使用此状态码便会返回200 OK的情况下才是合适的。
     */
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),

    /**
     * 对于该请求没有的内容可发送，但头部字段可能有用。用户代理可能会用此时请求头部信息来更新原来资源的头部缓存字段。
     */
    NO_CONTENT(204, "No Content"),

    /**
     * 告诉用户代理重置发送此请求的文档。
     */
    RESET_CONTENT(205, "Reset Content"),

    /**
     * 当从客户端发送Range范围标头以只请求资源的一部分时，将使用此响应代码。
     */
    PARTIAL_CONTENT(206, "Partial Content"),

    /**
     * 请求拥有多个可能的响应。用户代理或者用户应当从中选择一个。（没有标准化的方法来选择其中一个响应，但是建议使用指向可能性的 HTML 链接，以便用户可以选择。）
     */
    MULTIPLE_CHOICES(300, "Multiple Choices"),

    /**
     * 请求资源的 URL 已永久更改。在响应中给出了新的 URL。
     */
    MOVED_PERMANENTLY(301, "Moved Permanently"),

    /**
     * 此响应代码表示所请求资源的 URI 已 暂时 更改。未来可能会对 URI 进行进一步的改变。因此，客户机应该在将来的请求中使用这个相同的 URI。
     */
    FOUND(302, "Found"),

    /**
     * 服务器发送此响应，以指示客户端通过一个 GET 请求在另一个 URI 中获取所请求的资源。
     */
    SEE_OTHER(303, "See Other"),

    /**
     * 这是用于缓存的目的。它告诉客户端响应还没有被修改，因此客户端可以继续使用相同的缓存版本的响应。
     */
    NOT_MODIFIED(304, "Not Modified"),

    /**
     * 服务器发送此响应，以指示客户端使用在前一个请求中使用的相同方法在另一个 URI 上获取所请求的资源。这与 302 Found HTTP 响应代码具有相同的语义，但用户代理 不能 更改所使用的 HTTP 方法：如果在第一个请求中使用了 POST，则在第二个请求中必须使用 POST
     */
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),

    /**
     * 这意味着资源现在永久位于由Location: HTTP Response 标头指定的另一个 URI。这与 301 Moved Permanently HTTP 响应代码具有相同的语义，但用户代理不能更改所使用的 HTTP 方法：如果在第一个请求中使用 POST，则必须在第二个请求中使用 POST。
     */
    PERMANENT_REDIRECT(308, "Permanent Redirect"),

    /**
     * 由于被认为是客户端错误（例如，错误的请求语法、无效的请求消息帧或欺骗性的请求路由），服务器无法或不会处理请求。
     */
    BAD_REQUEST(400, "Bad Request"),

    /**
     * 虽然 HTTP 标准指定了"unauthorized"，但从语义上来说，这个响应意味着"unauthenticated"。也就是说，客户端必须对自身进行身份验证才能获得请求的响应。
     */
    UNAUTHORIZED(401, "Unauthorized"),

    /**
     * 此响应代码保留供将来使用。创建此代码的最初目的是将其用于数字支付系统，但是此状态代码很少使用，并且不存在标准约定。
     */
    PAYMENT_REQUIRED(402, "Payment Required"),

    /**
     * 客户端没有访问内容的权限；也就是说，它是未经授权的，因此服务器拒绝提供请求的资源。与 401 Unauthorized 不同，服务器知道客户端的身份。
     */
    FORBIDDEN(403, "Forbidden"),

    /**
     * 服务器找不到请求的资源。在浏览器中，这意味着无法识别 URL。在 API 中，这也可能意味着端点有效，但资源本身不存在。服务器也可以发送此响应，而不是 403 Forbidden，以向未经授权的客户端隐藏资源的存在。这个响应代码可能是最广为人知的，因为它经常出现在网络上。
     */
    NOT_FOUND(404, "Not Found"),

    /**
     * 服务器知道请求方法，但目标资源不支持该方法。例如，API 可能不允许调用DELETE来删除资源。
     */
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    /**
     * 当 web 服务器在执行服务端驱动型内容协商机制后，没有发现任何符合用户代理给定标准的内容时，就会发送此响应。
     */
    NOT_ACCEPTABLE(406, "Not Acceptable"),

    /**
     * 类似于 401 Unauthorized 但是认证需要由代理完成。
     */
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),

    /**
     * 此响应由一些服务器在空闲连接上发送，即使客户端之前没有任何请求。这意味着服务器想关闭这个未使用的连接。由于一些浏览器，如 Chrome、Firefox 27+ 或 IE9，使用 HTTP 预连接机制来加速冲浪，所以这种响应被使用得更多。还要注意的是，有些服务器只是关闭了连接而没有发送此消息。
     */
    REQUEST_TIMEOUT(408, "Request Timeout"),

    /**
     * 当请求与服务器的当前状态冲突时，将发送此响应。
     */
    CONFLICT(409, "Conflict"),

    /**
     * 当请求的内容已从服务器中永久删除且没有转发地址时，将发送此响应。客户端需要删除缓存和指向资源的链接。HTTP 规范打算将此状态代码用于“有限时间的促销服务”。API 不应被迫指出已使用此状态代码删除的资源。
     */
    GONE(410, "Gone"),

    /**
     * 服务端拒绝该请求因为 Content-Length 头部字段未定义但是服务端需要它。
     */
    LENGTH_REQUIRED(411, "Length Required"),

    /**
     * 客户端在其头文件中指出了服务器不满足的先决条件。
     */
    PRECONDITION_FAILED(412, "Precondition Failed"),

    /**
     * 请求实体大于服务器定义的限制。服务器可能会关闭连接，或在标头字段后返回重试 Retry-After。
     */
    CONTENT_TOO_LARGE(413, "Content Too Large"),

    /**
     * 客户端请求的 URI 比服务器愿意接收的长度长。
     */
    URI_TOO_LONG(414, "URI Too Long"),

    /**
     * 服务器不支持请求数据的媒体格式，因此服务器拒绝请求。
     */
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),

    /**
     * 无法满足请求中 Range 标头字段指定的范围。该范围可能超出了目标 URI 数据的大小。
     */
    RANGE_NOT_SATISFIABLE(416, "Range Not Satisfiable"),

    /**
     * 此响应代码表示服务器无法满足 Expect 请求标头字段所指示的期望。
     */
    EXPECTATION_FAILED(417, "Expectation Failed"),

    /**
     * 请求被定向到无法生成响应的服务器。这可以由未配置为针对请求 URI 中包含的方案和权限组合生成响应的服务器发送。
     */
    MISDIRECTED_REQUEST(421, "Misdirected Request"),

    /**
     * 请求格式正确，但由于语义错误而无法遵循。
     */
    UNPROCESSABLE_CONTENT(422, "Unprocessable Content"),

    /**
     * 服务器拒绝使用当前协议执行请求，但在客户端升级到其他协议后可能愿意这样做。 服务端发送带有Upgrade (en-US) 字段的 426 响应 来表明它所需的协议（们）。
     */
    UPGRADE_REQUIRED(426, "Upgrade Required"),

    /**
     * 表示在一定的时间内用户发送了太多的请求，即超出了“频次限制”。
     */
    TOO_MANY_REQUESTS(429, "Too Many Requests"),

    /**
     * 表示由于请求中的首部字段的值过大，服务器拒绝接受客户端的请求。客户端可以在缩减首部字段的体积后再次发送请求。
     */
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),

    /**
     * 服务器遇到了不知道如何处理的情况。
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    /**
     * 服务器不支持请求方法，因此无法处理。服务器需要支持的唯二方法（因此不能返回此代码）是 GET and HEAD.
     */
    NOT_IMPLEMENTED(501, "Not Implemented"),

    /**
     * 此错误响应表明服务器作为网关需要得到一个处理这个请求的响应，但是得到一个错误的响应。
     */
    BAD_GATEWAY(502, "Bad Gateway"),

    /**
     * 服务器没有准备好处理请求。常见原因是服务器因维护或重载而停机。请注意，与此响应一起，应发送解释问题的用户友好页面。这个响应应该用于临时条件和如果可能的话，HTTP 标头 Retry-After 字段应该包含恢复服务之前的估计时间。网站管理员还必须注意与此响应一起发送的与缓存相关的标头，因为这些临时条件响应通常不应被缓存。
     */
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    /**
     * 当服务器充当网关且无法及时获得响应时，会给出此错误响应。
     */
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),

    /**
     * 服务器不支持请求中使用的 HTTP 版本。
     */
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported");

    /**
     * 存储 code 和 对应枚举的映射
     */
    private static final HttpStatusCode[] MAP = initMap();

    private final int code;

    private final String description;

    HttpStatusCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    private static HttpStatusCode[] initMap() {
        var m = new HttpStatusCode[506];
        var values = HttpStatusCode.values();
        for (var v : values) {
            m[v.code] = v;
        }
        return m;
    }

    /**
     * @param code c
     * @return 未找到时 抛出异常
     */
    public static HttpStatusCode of(int code) {
        if (code < 0 || code > 505) {
            throw new IllegalArgumentException("Invalid HTTP status code: " + code);
        }
        var c = MAP[code];
        if (c == null) {
            throw new IllegalArgumentException("Invalid HTTP status code: " + code);
        }
        return c;
    }

    /**
     * @param code c
     * @return 未找到时 返回 null
     */
    public static HttpStatusCode find(int code) {
        if (code < 0 || code > 505) {
            return null;
        }
        return MAP[code];
    }

    public int code() {
        return code;
    }

    public String description() {
        return description;
    }

    @Override
    public String toString() {
        return code + " " + description;
    }

}
