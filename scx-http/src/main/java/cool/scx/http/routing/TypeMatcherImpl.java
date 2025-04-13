package cool.scx.http.routing;

import cool.scx.http.ScxHttpServerRequest;
//import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;

import static cool.scx.http.routing.TypeMatcher.Type.*;

public class TypeMatcherImpl implements TypeMatcher {

    static TypeMatcher ANY_TYPE = new TypeMatcherImpl(ANY);
    static TypeMatcher NORMAL_TYPE = new TypeMatcherImpl(NORMAL);
    static TypeMatcher WEB_SOCKET_HANDSHAKE_TYPE = new TypeMatcherImpl(WEB_SOCKET_HANDSHAKE);

    private final Type type;

    private TypeMatcherImpl(TypeMatcher.Type type) {
        this.type = type;
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public boolean matches(ScxHttpServerRequest request) {
        // todo 这里如何处理 不同类型?
//        return switch (type) {
//            case ANY -> true;
//            case NORMAL -> !(request instanceof ScxServerWebSocketHandshakeRequest);
//            case WEB_SOCKET_HANDSHAKE -> request instanceof ScxServerWebSocketHandshakeRequest;
//        };
        return true;
    }

}
