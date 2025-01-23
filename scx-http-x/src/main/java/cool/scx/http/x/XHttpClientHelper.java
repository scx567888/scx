package cool.scx.http.x;

import cool.scx.http.uri.ScxURI;

import java.net.InetSocketAddress;

class XHttpClientHelper {

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
        var scheme = uri.scheme().toLowerCase();
        if (port == -1) {
            port = switch (scheme) {
                case "http", "ws" -> 80;
                case "https", "wss" -> 443;
                default -> throw new IllegalArgumentException("Unsupported scheme: " + uri.scheme());
            };
        }
        return new InetSocketAddress(host, port);
    }

}
