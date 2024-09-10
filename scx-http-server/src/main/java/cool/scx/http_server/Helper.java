package cool.scx.http_server;

import io.helidon.common.uri.UriQuery;
import io.helidon.http.*;

import static cool.scx.http_server.ScxHttpMethodEnum.*;

public class Helper {

    public static ScxHttpMethod createScxHttpMethod(String httpMethod) {
        try {
            return ScxHttpMethodEnum.valueOf(httpMethod.toUpperCase());
        } catch (Exception e) {
            return new ScxHttpMethodImpl(httpMethod);
        }
    }

    public static ScxHttpMethod createScxHttpMethod(Method method) {
        if (method == Method.GET) {
            return GET;
        }
        if (method == Method.POST) {
            return POST;
        }
        if (method == Method.PUT) {
            return PUT;
        }
        if (method == Method.DELETE) {
            return DELETE;
        }
        if (method == Method.HEAD) {
            return HEAD;
        }
        if (method == Method.OPTIONS) {
            return OPTIONS;
        }
        if (method == Method.TRACE) {
            return TRACE;
        }
        if (method == Method.PATCH) {
            return PATCH;
        }
        if (method == Method.CONNECT) {
            return CONNECT;
        }
        return new ScxHttpMethodImpl(method.text());
    }

    public static ScxHttpPath createScxHttpPath(HttpPrologue prologue) {
        var query = createScxHttpPathQuery(prologue.query());
        var path = prologue.uriPath().path();
        return new ScxHttpPathImpl(query, path, null);
    }

    private static ScxHttpPathQuery createScxHttpPathQuery(UriQuery q) {
        var map = q.toMap();
        var httpPathQuery = new ScxHttpPathQueryImpl();
        for (var e : map.entrySet()) {
            var key = e.getKey();
            var values = e.getValue();
            for (var value : values) {
                httpPathQuery.add(key, value);
            }
        }
        return httpPathQuery;
    }

    public static ScxHttpVersion createScxHttpVersion(String version) {
        version = version.toUpperCase();
        return switch (version) {
            case "HTTP/1.1" -> ScxHttpVersion.HTTP_1_1;
            case "HTTP/2.0" -> ScxHttpVersion.HTTP_2;
            default -> throw new IllegalArgumentException("Unsupported HTTP version: " + version);
        };
    }

    public static ScxHttpHeaders createScxHttpHeaders(ServerRequestHeaders q) {
        var httpHeaders = new ScxHttpHeadersImpl();
        for (Header header : q) {
            httpHeaders.add(createScxHttpHeader(header));
        }
        return httpHeaders;
    }

    public static ScxHttpHeaderImpl createScxHttpHeader(Header h) {
        var scxHttpHeaderName = createScxHttpHeaderName(h.headerName());
        return new ScxHttpHeaderImpl(scxHttpHeaderName, h.allValues());
    }

    public static ScxHttpHeaderName createScxHttpHeaderName(HeaderName headerName) {
        try {
            return ScxHttpHeaderNameEnum.of(headerName.defaultCase());
        } catch (Exception e) {
            return new ScxHttpHeaderNameImpl(headerName.defaultCase());
        }
    }

}
