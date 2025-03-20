package cool.scx.http.status;

import java.util.EnumMap;

import static cool.scx.http.status.HttpStatus.*;

public final class ScxHttpStatusHelper {

    private static final EnumMap<HttpStatus, String> MAP = initMap();

    private static EnumMap<HttpStatus, String> initMap() {
        var m = new EnumMap<HttpStatus, String>(HttpStatus.class);
        m.put(CONTINUE, "Continue");
        m.put(SWITCHING_PROTOCOLS, "Switching Protocols");
        m.put(OK, "OK");
        m.put(CREATED, "Created");
        m.put(ACCEPTED, "Accepted");
        m.put(NON_AUTHORITATIVE_INFORMATION, "Non-Authoritative Information");
        m.put(NO_CONTENT, "No Content");
        m.put(RESET_CONTENT, "Reset Content");
        m.put(PARTIAL_CONTENT, "Partial Content");
        m.put(MULTIPLE_CHOICES, "Multiple Choices");
        m.put(MOVED_PERMANENTLY, "Moved Permanently");
        m.put(FOUND, "Found");
        m.put(SEE_OTHER, "See Other");
        m.put(NOT_MODIFIED, "Not Modified");
        m.put(TEMPORARY_REDIRECT, "Temporary Redirect");
        m.put(PERMANENT_REDIRECT, "Permanent Redirect");
        m.put(BAD_REQUEST, "Bad Request");
        m.put(UNAUTHORIZED, "Unauthorized");
        m.put(PAYMENT_REQUIRED, "Payment Required");
        m.put(FORBIDDEN, "Forbidden");
        m.put(NOT_FOUND, "Not Found");
        m.put(METHOD_NOT_ALLOWED, "Method Not Allowed");
        m.put(NOT_ACCEPTABLE, "Not Acceptable");
        m.put(PROXY_AUTHENTICATION_REQUIRED, "Proxy Authentication Required");
        m.put(REQUEST_TIMEOUT, "Request Timeout");
        m.put(CONFLICT, "Conflict");
        m.put(GONE, "Gone");
        m.put(LENGTH_REQUIRED, "Length Required");
        m.put(PRECONDITION_FAILED, "Precondition Failed");
        m.put(CONTENT_TOO_LARGE, "Content Too Large");
        m.put(URI_TOO_LONG, "URI Too Long");
        m.put(UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
        m.put(RANGE_NOT_SATISFIABLE, "Range Not Satisfiable");
        m.put(EXPECTATION_FAILED, "Expectation Failed");
        m.put(MISDIRECTED_REQUEST, "Misdirected Request");
        m.put(UNPROCESSABLE_CONTENT, "Unprocessable Content");
        m.put(UPGRADE_REQUIRED, "Upgrade Required");
        m.put(TOO_MANY_REQUESTS, "Too Many Requests");
        m.put(REQUEST_HEADER_FIELDS_TOO_LARGE, "Request Header Fields Too Large");
        m.put(INTERNAL_SERVER_ERROR, "Internal Server Error");
        m.put(NOT_IMPLEMENTED, "Not Implemented");
        m.put(BAD_GATEWAY, "Bad Gateway");
        m.put(SERVICE_UNAVAILABLE, "Service Unavailable");
        m.put(GATEWAY_TIMEOUT, "Gateway Timeout");
        m.put(HTTP_VERSION_NOT_SUPPORTED, "HTTP Version Not Supported");
        return m;
    }

    public static String getReasonPhrase(HttpStatus status) {
        return MAP.get(status);
    }

    public static String getReasonPhrase(ScxHttpStatus status, String defaultReasonPhrase) {
        return status instanceof HttpStatus s ? getReasonPhrase(s) : defaultReasonPhrase;
    }

}
