package cool.scx.http.routing;

import cool.scx.http.ScxHttpServerRequest;

public interface TypeMatcher {

    static TypeMatcher of(Type type) {
        return switch (type) {
            case ANY -> any();
            case NORMAL -> normal();
            case WEB_SOCKET_HANDSHAKE -> webSocketHandshake();
        };
    }

    static TypeMatcher any() {
        return TypeMatcherImpl.ANY_TYPE;
    }

    static TypeMatcher normal() {
        return TypeMatcherImpl.NORMAL_TYPE;
    }

    static TypeMatcher webSocketHandshake() {
        return TypeMatcherImpl.WEB_SOCKET_HANDSHAKE_TYPE;
    }

    Type type();

    boolean matches(ScxHttpServerRequest request);

    enum Type {
        ANY,
        NORMAL,
        WEB_SOCKET_HANDSHAKE,
    }

}
