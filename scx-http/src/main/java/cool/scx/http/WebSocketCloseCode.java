package cool.scx.http;

public enum WebSocketCloseCode {

    /**
     * Code to use with {@link ScxWebSocket#close(int, String)} to indicate normal close operation.
     */
    NORMAL_CLOSE(1000, "normal close"),

    /**
     * Client is leaving (browser tab closing).
     */
    GOING_AWAY(1001, "going away"),

    /**
     * Endpoint received a malformed frame.
     */
    PROTOCOL_ERROR(1002, "protocol error"),

    /**
     * Endpoint received an unsupported frame (e.g. binary-only endpoint received text frame).
     */
    CANNOT_ACCEPT(1003, "cannot accept message"),

    /**
     * Expected close status, received none.
     */
    NO_STATUS_CODE(1005, "no status code"),

    /**
     * No close code frame has been received.
     */
    CLOSED_ABNORMALLY(1006, "closed abnormally"),

    /**
     * Endpoint received inconsistent message (e.g. malformed UTF-8).
     */
    NOT_CONSISTENT(1007, "not consistent"),

    /**
     * Generic code used for situations other than 1003 and 1009.
     */
    VIOLATED_POLICY(1008, "violated policy"),

    /**
     * Endpoint won't process large frame.
     */
    TOO_BIG(1009, "too big"),

    /**
     * Client wanted an extension which server did not negotiate.
     */
    NO_EXTENSION(1010, "no extension"),

    /**
     * Internal server error while operating.
     */
    UNEXPECTED_CONDITION(1011, "unexpected condition"),

    /**
     * Server/service is restarting.
     */
    SERVICE_RESTART(1012, "service restart"),

    /**
     * Temporary server condition forced blocking client's request.
     */
    TRY_AGAIN_LATER(1013, "try again later"),

    /**
     * Server acting as gateway received an invalid response.
     */
    BAD_GATEWAY(1014, "bad gateway"),

    /**
     * Transport Layer Security handshake failure.
     */
    TLS_HANDSHAKE_FAIL(1015, "tls handshake fail"),

    /**
     * Reserved for later min value.
     */
    RESERVED_FOR_LATER_MIN(1016, "reserved for later min"),

    /**
     * Reserved for later max value.
     */
    RESERVED_FOR_LATER_MAX(1999, "reserved for later max"),

    /**
     * Reserved for extensions min value.
     */
    RESERVED_FOR_EXTENSIONS_MIN(2000, "reserved for extensions min"),

    /**
     * Reserved for extensions max value.
     */
    RESERVED_FOR_EXTENSIONS_MAX(2999, "reserved for extensions max"),

    /**
     * Registered at IANA min value.
     */
    REGISTERED_AT_IANA_MIN(3000, "registered at iana min"),

    /**
     * Registered at IANA max value.
     */
    REGISTERED_AT_IANA_MAX(3999, "registered at iana max"),

    /**
     * Application min value.
     */
    APPLICATION_MIN(4000, "application min"),

    /**
     * Application max value.
     */
    APPLICATION_MAX(4999, "application max");

    private final int code;

    private final String reason;

    WebSocketCloseCode(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int code() {
        return code;
    }

    public String reason() {
        return reason;
    }

}
