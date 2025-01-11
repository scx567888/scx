package cool.scx.http.usagi.http1x;

import cool.scx.http.HttpVersion;
import cool.scx.http.ScxHttpMethod;

import java.net.URLDecoder;
import java.net.URLEncoder;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class Http1xRequestLineHelper {

    public static Http1xRequestLine parseRequestLine(String requestLineStr) {
        var parts = requestLineStr.split(" ");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid request line: " + requestLineStr);
        }

        var methodStr = parts[0];
        var pathStr = parts[1];
        var versionStr = parts[2];

        var method = ScxHttpMethod.of(methodStr);
        var path = URLDecoder.decode(pathStr, UTF_8);
        var version = HttpVersion.of(versionStr);

        return new Http1xRequestLine(method, path, version);
    }

    public static String encodeRequestLine(Http1xRequestLine requestLine) {
        var method = requestLine.method().value();
        var path = URLEncoder.encode(requestLine.path(), UTF_8);
        var version = requestLine.version().value();
        return method + " " + path + " " + version;
    }

}
