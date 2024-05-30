package cool.scx.common.standard;

/**
 * HttpFieldName
 *
 * @author scx567888
 * @version 0.3.6
 * @see <a href="https://www.iana.org/assignments/http-fields/http-fields.xhtml">https://www.iana.org/assignments/http-fields/http-fields.xhtml</a>
 */
public enum HttpFieldName {

    /**
     * 用来告知（服务器）客户端可以处理的内容类型
     */
    ACCEPT("Accept"),

    /**
     * 会将客户端能够理解的内容编码方式——通常是某种压缩算法——进行通知（给服务端）
     */
    ACCEPT_ENCODING("Accept-Encoding"),

    /**
     * 允许客户端声明它可以理解的自然语言，以及优先选择的区域方言
     */
    ACCEPT_LANGUAGE("Accept-Language"),

    /**
     * 通知浏览器请求的媒体类型 (media-type) 可以被服务器理解
     */
    ACCEPT_PATCH("Accept-Patch"),

    /**
     * advertises which media types are accepted by the server for HTTP post requests.
     */
    ACCEPT_POST("Accept-Post"),

    /**
     * 标识自身支持范围请求
     */
    ACCEPT_RANGES("Accept-Ranges"),

    /**
     * 用于在请求要求包含 credentials（Request.credentials 的值为 include）时，告知浏览器是否可以将对请求的响应暴露给前端 JavaScript 代码。
     */
    ACCESS_CONTROL_ALLOW_CREDENTIALS("Access-Control-Allow-Credentials"),

    /**
     * 用于 preflight request（预检请求）中，列出了将会在正式请求的 Access-Control-Request-Headers 字段中出现的首部信息。
     */
    ACCESS_CONTROL_ALLOW_HEADERS("Access-Control-Allow-Headers"),

    /**
     * 在对 preflight request.（预检请求）的应答中明确了客户端所要访问的资源允许使用的方法或方法列表。
     */
    ACCESS_CONTROL_ALLOW_METHODS("Access-Control-Allow-Methods"),

    /**
     * 响应标头指定了该响应的资源是否被允许与给定的来源（origin）共享。
     */
    ACCESS_CONTROL_ALLOW_ORIGIN("Access-Control-Allow-Origin"),

    /**
     * 允许服务器指示那些响应标头可以暴露给浏览器中运行的脚本，以响应跨源请求。
     */
    ACCESS_CONTROL_EXPOSE_HEADERS("Access-Control-Expose-Headers"),

    /**
     * 表示 preflight request （预检请求）的返回结果（即 Access-Control-Allow-Methods 和Access-Control-Allow-Headers 提供的信息）可以被缓存多久
     */
    ACCESS_CONTROL_MAX_AGE("Access-Control-Max-Age"),

    /**
     * 出现于 preflight request（预检请求）中，用于通知服务器在真正的请求中会采用哪些请求头。
     */
    ACCESS_CONTROL_REQUEST_HEADERS("Access-Control-Request-Headers"),

    /**
     * 出现于 preflight request（预检请求）中，用于通知服务器在真正的请求中会采用哪种 HTTP 方法。因为预检请求所使用的方法总是 OPTIONS ，与实际请求所使用的方法不一样，所以这个请求头是必要的。
     */
    ACCESS_CONTROL_REQUEST_METHOD("Access-Control-Request-Method"),

    /**
     * 消息头里包含对象在缓存代理中存贮的时长，以秒为单位。
     */
    AGE("Age"),

    /**
     * 用于枚举资源所支持的 HTTP 方法的集合。
     */
    ALLOW("Allow"),

    /**
     * 请求标头用于提供服务器验证用户代理身份的凭据，允许访问受保护的资源。
     */
    AUTHORIZATION("Authorization"),

    /**
     * 通用消息头字段，被用于在 http 请求和响应中，通过指定指令来实现缓存机制。缓存指令是单向的，这意味着在请求中设置的指令，不一定被包含在响应中。
     */
    CACHE_CONTROL("Cache-Control"),

    /**
     * 通用标头控制网络连接在当前会话完成后是否仍然保持打开状态。如果发送的值是 keep-alive，则连接是持久的，不会关闭，允许对同一服务器进行后续请求。
     */
    CONNECTION("Connection"),

    /**
     * 指示回复的内容该以何种形式展示，是以内联的形式（即网页或者页面的一部分），还是以附件的形式下载并保存到本地。
     */
    CONTENT_DISPOSITION("Content-Disposition"),

    /**
     * 列出了对当前实体消息（消息荷载）应用的任何编码类型，以及编码的顺序。它让接收者知道需要以何种顺序解码该实体消息才能获得原始荷载格式。Content-Encoding 主要用于在不丢失原媒体类型内容的情况下压缩消息数据。
     */
    CONTENT_ENCODING("Content-Encoding"),

    /**
     * 用来说明访问者希望采用的语言或语言组合，这样的话用户就可以根据自己偏好的语言来定制不同的内容。
     */
    CONTENT_LANGUAGE("Content-Language"),

    /**
     * 用来指明发送给接收方的消息主体的大小，即用十进制数字表示的八位元组的数目。
     */
    CONTENT_LENGTH("Content-Length"),

    /**
     * 首部指定的是要返回的数据的地址选项。最主要的用途是用来指定要访问的资源经过内容协商后的结果的 URL。
     */
    CONTENT_LOCATION("Content-Location"),

    /**
     * 显示的是一个数据片段在整个文件中的位置
     */
    CONTENT_RANGE("Content-Range"),

    /**
     * 实体头部用于指示资源的 MIME 类型 media type 。
     */
    CONTENT_TYPE("Content-Type"),

    /**
     * 其中含有先前由服务器通过 Set-Cookie 标头投放或通过 JavaScript 的 Document.cookie 方法设置，然后存储到客户端的 HTTP cookie 。
     */
    COOKIE("Cookie"),

    /**
     * 其中包含了报文创建的日期和时间。
     */
    DATE("Date"),

    /**
     * 资源的特定版本的标识符。这可以让缓存更高效，并节省带宽，因为如果内容没有改变，Web 服务器不需要发送完整的响应。而如果内容发生了变化，使用 ETag 有助于防止资源的同时更新相互覆盖（“空中碰撞”）
     */
    ETAG("ETag"),

    /**
     * 响应头包含日期/时间，即在此时候之后，响应过期。
     */
    EXPIRES("Expires"),

    /**
     * 首部中包含了代理服务器的客户端的信息，即由于代理服务器在请求路径中的介入而被修改或丢失的信息。
     */
    FORWARDED("Forwarded"),

    /**
     * 包含一个电子邮箱地址，这个电子邮箱地址属于发送请求的用户代理的实际掌控者的人类用户。
     */
    FROM("From"),

    /**
     * 指明了请求将要发送到的服务器主机名和端口号。
     */
    HOST("Host"),

    /**
     * 这是一个条件请求。在请求方法为 GET 和 HEAD 的情况下，服务器仅在请求的资源满足此首部列出的 ETag值时才会返回资源。而对于 PUT 或其他非安全方法来说，只有在满足条件的情况下才可以将资源上传。
     */
    IF_MATCH("If-Match"),

    /**
     * 是一个条件式请求首部，服务器只在所请求的资源在给定的日期时间之后对内容进行过修改的情况下才会将资源返回，状态码为 200 。如果请求的资源从那时起未经修改，那么返回一个不带有消息主体的 304 响应，而在 Last-Modified 首部中会带有上次修改时间。不同于 If-Unmodified-Since, If-Modified-Since 只可以用在 GET 或 HEAD 请求中。
     */
    IF_MODIFIED_SINCE("If-Modified-Since"),

    /**
     * 是一个条件式请求首部。对于 GET 和 HEAD 请求方法来说，当且仅当服务器上没有任何资源的 ETag 属性值与这个首部中列出的相匹配的时候，服务器端才会返回所请求的资源，响应码为 200 。对于其他方法来说，当且仅当最终确认没有已存在的资源的 ETag 属性值与这个首部中所列出的相匹配的时候，才会对请求进行相应的处理。
     */
    IF_NONE_MATCH("If-None-Match"),

    /**
     * If-Range HTTP 请求头字段用来使得 Range 头字段在一定条件下起作用：当字段值中的条件得到满足时，Range 头字段才会起作用，同时服务器回复206 部分内容状态码，以及**Range** 头字段请求的相应部分；如果字段值中的条件没有得到满足，服务器将会返回 200 OK 状态码，并返回完整的请求资源。
     */
    IF_RANGE("If-Range"),

    /**
     * 用于请求之中，使得当前请求成为条件式请求：只有当资源在指定的时间之后没有进行过修改的情况下，服务器才会返回请求的资源，或是接受 POST 或其他 non-safe 方法的请求。如果所请求的资源在指定的时间之后发生了修改，那么会返回 412 (Precondition Failed) 错误。
     */
    IF_UNMODIFIED_SINCE("If-Unmodified-Since"),

    /**
     * 包含源头服务器认定的资源做出修改的日期及时间。它通常被用作一个验证器来判断接收到的或者存储的资源是否彼此一致。由于精确度比 ETag 要低，所以这是一个备用机制。包含有 If-Modified-Since 或 If-Unmodified-Since 首部的条件请求会使用这个字段。
     */
    LAST_MODIFIED("Last-Modified"),

    /**
     * 首部指定的是需要将页面重新定向至的地址。一般在响应码为 3xx 的响应中才会有意义。
     */
    LOCATION("Location"),

    /**
     * 请求标头被用于限制 TRACE 方法可经过的服务器（通常指代理服务器）数目。它的值是一个整数，指定可经过的服务器最大数目。服务器在转发 TRACE 请求之前，将递减 Max-Forwards 的值，直到到达目标服务器，或服务器接收到 Max-Forwards 的值为 0 的请求。而后直接返回一个 200 OK 的响应（可以包含一些标头）。
     */
    MAX_FORWARDS("Max-Forwards"),

    /**
     * 表示了请求的来源（协议、主机、端口）。例如，如果一个用户代理需要请求一个页面中包含的资源，或者执行脚本中的 HTTP 请求（fetch），那么该页面的来源（origin）就可能被包含在这次请求中。
     */
    ORIGIN("Origin"),

    /**
     * 指定了获取 proxy server（代理服务器）上的资源访问权限而采用的身份验证方式。代理服务器对请求进行验证，以便它进一步传递请求。
     */
    PROXY_AUTHENTICATE("Proxy-Authenticate"),

    /**
     * 是一个请求首部，其中包含了用户代理提供给代理服务器的用于身份验证的凭证。这个首部通常是在服务器返回了 407 Proxy Authentication Required 响应状态码及 Proxy-Authenticate 首部后发送的。
     */
    PROXY_AUTHORIZATION("Proxy-Authorization"),

    /**
     * 是一个请求首部，告知服务器返回文件的哪一部分。在一个 Range 首部中，可以一次性请求多个部分，服务器会以 multipart 文件的形式将其返回。如果服务器返回的是范围响应，需要使用 206 Partial Content 状态码。假如所请求的范围不合法，那么服务器会返回 416 Range Not Satisfiable 状态码，表示客户端错误。服务器允许忽略 Range 首部，从而返回整个文件，状态码用 200 。
     */
    RANGE("Range"),

    /**
     *
     */
    REFERER("Referer"),

    /**
     * 用在 websocket 开放握手中。它会出现在响应头中。也就是说，这是由服务器发送到客户端的头（header），用以告知服务器愿发起一个 websocket 连接。
     */
    SEC_WEBSOCKET_ACCEPT("Sec-WebSocket-Accept"),

    /**
     * 首部包含了处理请求的源头服务器所用到的软件相关信息。
     */
    SERVER("Server"),

    /**
     * HTTP 响应标头用于将 cookie 由服务器发送到用户代理，以便用户代理在后续的请求中可以将其发送回服务器。要发送多个 cookie，则应在同一响应中发送多个 Set-Cookie 标头。
     */
    SET_COOKIE("Set-Cookie"),

    /**
     * 响应标头用来通知浏览器应该只通过 HTTPS 访问该站点，并且以后使用 HTTP 访问该站点的所有尝试都应自动重定向到 HTTPS。
     */
    STRICT_TRANSPORT_SECURITY("Strict-Transport-Security"),

    /**
     * 用来指定用户代理希望使用的传输编码类型。
     */
    TE("TE"),

    /**
     * 允许发送方在分块发送的消息后面添加额外的元信息，这些元信息可能是随着消息主体的发送动态生成的，比如消息的完整性校验，消息的数字签名，或者消息经过处理之后的最终状态等
     */
    TRAILER("Trailer"),

    /**
     * 指明了将 entity 安全传递给用户所采用的编码形式。
     */
    TRANSFER_ENCODING("Transfer-Encoding"),

    /**
     * The HTTP 1.1 (only) Upgrade header can be used to upgrade an already established client/server connection to a different protocol (over the same transport protocol). For example, it can be used by a client to upgrade a connection from HTTP 1.1 to HTTP 2.0, or an HTTP or HTTPS connection into a WebSocket.
     */
    UPGRADE("Upgrade"),

    /**
     * 首部包含了一个特征字符串，用来让网络协议的对端来识别发起请求的用户代理软件的应用类型、操作系统、软件开发商以及版本号。
     */
    USER_AGENT("User-Agent"),

    /**
     * 描述了除方法和 URL 之外影响响应内容的请求消息。大多数情况下，这用于在使用内容协商时创建缓存键。
     */
    VARY("Vary"),

    /**
     * 由代理服务器添加的，适用于正向和反向代理，在请求和响应首部中均可出现。这个消息首部可以用来追踪消息转发情况，防止循环请求，以及识别在请求或响应传递链中消息发送者对于协议的支持能力。
     */
    VIA("Via"),

    /**
     * WWW_Authenticate
     */
    WWW_AUTHENTICATE("WWW-Authenticate");

    private final String value;

    HttpFieldName(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
