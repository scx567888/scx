package cool.scx.http.x;

import cool.scx.http.uri.ScxURI;

import java.net.InetSocketAddress;

public class HttpClientHelper {

    public static boolean checkIsTLS(ScxURI uri) {
        var scheme = uri.scheme().toLowerCase();
        return switch (scheme) {
            case "http", "ws" -> false;
            case "https", "wss" -> true;
            default -> throw new IllegalArgumentException("Unsupported scheme: " + uri.scheme());
        };
    }

    public static InetSocketAddress getRemoteAddress(ScxURI uri) {
        var host = uri.host();
        var port = uri.port();
        if (port == null) {
            port = getDefaultPort(uri.scheme());
        }
        return new InetSocketAddress(host, port);
    }

    public static int getDefaultPort(String scheme) {
        scheme = scheme.toLowerCase();
        return switch (scheme) {
            case "http", "ws" -> 80;
            case "https", "wss" -> 443;
            default -> throw new IllegalArgumentException("Unsupported scheme: " + scheme);
        };
    }

}
