package cool.scx.http_server;

import io.helidon.http.HttpPrologue;
import io.helidon.http.Method;

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
        var query = createScxHttpPathQuery();
        String s = prologue.uriPath().rawPath();
        return new ScxHttpPathImpl(query, null, null);
    }

    private static Object createScxHttpPathQuery() {
        return null;
    }

    public static ScxHttpVersion createScxHttpVersion(String version) {
        version = version.toUpperCase();
        return switch (version) {
            case "HTTP/1.1" -> ScxHttpVersion.HTTP_1_1;
            case "HTTP/2.0" -> ScxHttpVersion.HTTP_2;
            default -> throw new IllegalArgumentException("Unsupported HTTP version: " + version);
        };
    }

}
