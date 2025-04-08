package cool.scx.http.headers;

import java.util.HashMap;
import java.util.Map;

/// HttpFieldName
///
/// @author scx567888
/// @version 0.0.1
/// @see <a href="https://www.iana.org/assignments/http-fields/http-fields.xhtml">https://www.iana.org/assignments/http-fields/http-fields.xhtml</a>
public enum HttpFieldName implements ScxHttpHeaderName {

    ACCEPT("Accept"),
    ACCEPT_ENCODING("Accept-Encoding"),
    ACCEPT_LANGUAGE("Accept-Language"),
    ACCEPT_PATCH("Accept-Patch"),
    ACCEPT_POST("Accept-Post"),
    ACCEPT_RANGES("Accept-Ranges"),
    ACCESS_CONTROL_ALLOW_CREDENTIALS("Access-Control-Allow-Credentials"),
    ACCESS_CONTROL_ALLOW_HEADERS("Access-Control-Allow-Headers"),
    ACCESS_CONTROL_ALLOW_METHODS("Access-Control-Allow-Methods"),
    ACCESS_CONTROL_ALLOW_ORIGIN("Access-Control-Allow-Origin"),
    ACCESS_CONTROL_EXPOSE_HEADERS("Access-Control-Expose-Headers"),
    ACCESS_CONTROL_MAX_AGE("Access-Control-Max-Age"),
    ACCESS_CONTROL_REQUEST_HEADERS("Access-Control-Request-Headers"),
    ACCESS_CONTROL_REQUEST_METHOD("Access-Control-Request-Method"),
    AGE("Age"),
    ALLOW("Allow"),
    AUTHORIZATION("Authorization"),
    CACHE_CONTROL("Cache-Control"),
    CONNECTION("Connection"),
    CONTENT_DISPOSITION("Content-Disposition"),
    CONTENT_ENCODING("Content-Encoding"),
    CONTENT_LANGUAGE("Content-Language"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_LOCATION("Content-Location"),
    CONTENT_RANGE("Content-Range"),
    CONTENT_TYPE("Content-Type"),
    COOKIE("Cookie"),
    DATE("Date"),
    ETAG("ETag"),
    EXPECT("Expect"),
    EXPIRES("Expires"),
    FORWARDED("Forwarded"),
    FROM("From"),
    HOST("Host"),
    IF_MATCH("If-Match"),
    IF_MODIFIED_SINCE("If-Modified-Since"),
    IF_NONE_MATCH("If-None-Match"),
    IF_RANGE("If-Range"),
    IF_UNMODIFIED_SINCE("If-Unmodified-Since"),
    LAST_MODIFIED("Last-Modified"),
    LOCATION("Location"),
    MAX_FORWARDS("Max-Forwards"),
    ORIGIN("Origin"),
    PROXY_AUTHENTICATE("Proxy-Authenticate"),
    PROXY_AUTHORIZATION("Proxy-Authorization"),
    RANGE("Range"),
    REFERER("Referer"),
    SEC_WEBSOCKET_ACCEPT("Sec-WebSocket-Accept"),
    SEC_WEBSOCKET_KEY("Sec-WebSocket-Key"),
    SEC_WEBSOCKET_PROTOCOL("Sec-WebSocket-Protocol"),
    SEC_WEBSOCKET_VERSION("Sec-WebSocket-Version"),
    SERVER("Server"),
    SET_COOKIE("Set-Cookie"),
    STRICT_TRANSPORT_SECURITY("Strict-Transport-Security"),
    TE("TE"),
    TRAILER("Trailer"),
    TRANSFER_ENCODING("Transfer-Encoding"),
    UPGRADE("Upgrade"),
    USER_AGENT("User-Agent"),
    VARY("Vary"),
    VIA("Via"),
    WWW_AUTHENTICATE("WWW-Authenticate");

    private static final Map<String, HttpFieldName> MAP = initMap();

    private final String value;

    HttpFieldName(String value) {
        this.value = value;
    }

    private static Map<String, HttpFieldName> initMap() {
        var map = new HashMap<String, HttpFieldName>();
        for (var h : HttpFieldName.values()) {
            map.put(h.value().toLowerCase(), h);
        }
        return map;
    }

    /// @param v v
    /// @return 未找到 抛出异常
    public static HttpFieldName of(String v) {
        var h = find(v);
        if (h == null) {
            throw new IllegalArgumentException(v);
        }
        return h;
    }

    /// @param v v
    /// @return 未找到返回 null
    public static HttpFieldName find(String v) {
        var lowerCase = v.toLowerCase();
        return MAP.get(lowerCase);
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
    
}
