package cool.scx.enumeration;

/**
 * 规定一些常见的 http 相应状态
 *
 * @author scx567888
 * @version 1.11.8
 */
public enum HttpResponseStatus {

    CONTINUE(100, "Continue"),
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),
    PROCESSING(102, "Processing"),
    EARLY_HINTS(103, "Early Hints"),
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
    NO_CONTENT(204, "No Content"),
    RESET_CONTENT(205, "Reset Content"),
    PARTIAL_CONTENT(206, "Partial Content"),
    MULTI_STATUS(207, "Multi-Status"),
    MULTIPLE_CHOICES(300, "Multiple Choices"),
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    FOUND(302, "Found"),
    SEE_OTHER(303, "See Other"),
    NOT_MODIFIED(304, "Not Modified"),
    USE_PROXY(305, "Use Proxy"),
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),
    PERMANENT_REDIRECT(308, "Permanent Redirect"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    PAYMENT_REQUIRED(402, "Payment Required"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    REQUEST_TIMEOUT(408, "Request Timeout"),
    CONFLICT(409, "Conflict"),
    GONE(410, "Gone"),
    LENGTH_REQUIRED(411, "Length Required"),
    PRECONDITION_FAILED(412, "Precondition Failed"),
    REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
    REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),
    EXPECTATION_FAILED(417, "Expectation Failed"),
    MISDIRECTED_REQUEST(421, "Misdirected Request"),
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
    LOCKED(423, "Locked"),
    FAILED_DEPENDENCY(424, "Failed Dependency"),
    UNORDERED_COLLECTION(425, "Unordered Collection"),
    UPGRADE_REQUIRED(426, "Upgrade Required"),
    PRECONDITION_REQUIRED(428, "Precondition Required"),
    TOO_MANY_REQUESTS(429, "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),
    INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
    NOT_EXTENDED(510, "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");

    private final int statusCode;

    private final String reasonPhrase;

    HttpResponseStatus(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    public static HttpResponseStatus of(int code) {
        return switch (code) {
            case 100 -> CONTINUE;
            case 101 -> SWITCHING_PROTOCOLS;
            case 102 -> PROCESSING;
            case 103 -> EARLY_HINTS;
            case 200 -> OK;
            case 201 -> CREATED;
            case 202 -> ACCEPTED;
            case 203 -> NON_AUTHORITATIVE_INFORMATION;
            case 204 -> NO_CONTENT;
            case 205 -> RESET_CONTENT;
            case 206 -> PARTIAL_CONTENT;
            case 207 -> MULTI_STATUS;
            case 300 -> MULTIPLE_CHOICES;
            case 301 -> MOVED_PERMANENTLY;
            case 302 -> FOUND;
            case 303 -> SEE_OTHER;
            case 304 -> NOT_MODIFIED;
            case 305 -> USE_PROXY;
            case 307 -> TEMPORARY_REDIRECT;
            case 308 -> PERMANENT_REDIRECT;
            case 400 -> BAD_REQUEST;
            case 401 -> UNAUTHORIZED;
            case 402 -> PAYMENT_REQUIRED;
            case 403 -> FORBIDDEN;
            case 404 -> NOT_FOUND;
            case 405 -> METHOD_NOT_ALLOWED;
            case 406 -> NOT_ACCEPTABLE;
            case 407 -> PROXY_AUTHENTICATION_REQUIRED;
            case 408 -> REQUEST_TIMEOUT;
            case 409 -> CONFLICT;
            case 410 -> GONE;
            case 411 -> LENGTH_REQUIRED;
            case 412 -> PRECONDITION_FAILED;
            case 413 -> REQUEST_ENTITY_TOO_LARGE;
            case 414 -> REQUEST_URI_TOO_LONG;
            case 415 -> UNSUPPORTED_MEDIA_TYPE;
            case 416 -> REQUESTED_RANGE_NOT_SATISFIABLE;
            case 417 -> EXPECTATION_FAILED;
            case 421 -> MISDIRECTED_REQUEST;
            case 422 -> UNPROCESSABLE_ENTITY;
            case 423 -> LOCKED;
            case 424 -> FAILED_DEPENDENCY;
            case 425 -> UNORDERED_COLLECTION;
            case 426 -> UPGRADE_REQUIRED;
            case 428 -> PRECONDITION_REQUIRED;
            case 429 -> TOO_MANY_REQUESTS;
            case 431 -> REQUEST_HEADER_FIELDS_TOO_LARGE;
            case 500 -> INTERNAL_SERVER_ERROR;
            case 501 -> NOT_IMPLEMENTED;
            case 502 -> BAD_GATEWAY;
            case 503 -> SERVICE_UNAVAILABLE;
            case 504 -> GATEWAY_TIMEOUT;
            case 505 -> HTTP_VERSION_NOT_SUPPORTED;
            case 506 -> VARIANT_ALSO_NEGOTIATES;
            case 507 -> INSUFFICIENT_STORAGE;
            case 510 -> NOT_EXTENDED;
            case 511 -> NETWORK_AUTHENTICATION_REQUIRED;
            default -> null;
        };
    }

    public int statusCode() {
        return statusCode;
    }

    public String reasonPhrase() {
        return reasonPhrase;
    }

}
