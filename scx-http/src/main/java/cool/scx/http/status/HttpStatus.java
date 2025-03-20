package cool.scx.http.status;

/// HttpStatus
///
/// @author scx567888
/// @version 0.0.1
/// @see <a href="https://www.rfc-editor.org/rfc/rfc9110#name-status-codes">https://www.rfc-editor.org/rfc/rfc9110#name-status-codes</a>
public enum HttpStatus implements ScxHttpStatus {

    CONTINUE(100),
    SWITCHING_PROTOCOLS(101),
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NON_AUTHORITATIVE_INFORMATION(203),
    NO_CONTENT(204),
    RESET_CONTENT(205),
    PARTIAL_CONTENT(206),
    MULTIPLE_CHOICES(300),
    MOVED_PERMANENTLY(301),
    FOUND(302),
    SEE_OTHER(303),
    NOT_MODIFIED(304),
    TEMPORARY_REDIRECT(307),
    PERMANENT_REDIRECT(308),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    PAYMENT_REQUIRED(402),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    NOT_ACCEPTABLE(406),
    PROXY_AUTHENTICATION_REQUIRED(407),
    REQUEST_TIMEOUT(408),
    CONFLICT(409),
    GONE(410),
    LENGTH_REQUIRED(411),
    PRECONDITION_FAILED(412),
    CONTENT_TOO_LARGE(413),
    URI_TOO_LONG(414),
    UNSUPPORTED_MEDIA_TYPE(415),
    RANGE_NOT_SATISFIABLE(416),
    EXPECTATION_FAILED(417),
    MISDIRECTED_REQUEST(421),
    UNPROCESSABLE_CONTENT(422),
    UPGRADE_REQUIRED(426),
    TOO_MANY_REQUESTS(429),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431),
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503),
    GATEWAY_TIMEOUT(504),
    HTTP_VERSION_NOT_SUPPORTED(505);

    /// 存储 code 和 对应枚举的映射
    private static final HttpStatus[] MAP = initMap();

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }

    private static HttpStatus[] initMap() {
        var m = new HttpStatus[506];
        var values = HttpStatus.values();
        for (var v : values) {
            m[v.code] = v;
        }
        return m;
    }

    /// @param code c
    /// @return 未找到时 抛出异常
    public static HttpStatus of(int code) {
        var c = find(code);
        if (c == null) {
            throw new IllegalArgumentException("Invalid HTTP status code: " + code);
        }
        return c;
    }

    /// @param code c
    /// @return 未找到时 返回 null
    public static HttpStatus find(int code) {
        if (code < 0 || code > 505) {
            return null;
        }
        return MAP[code];
    }

    @Override
    public int code() {
        return code;
    }

}
