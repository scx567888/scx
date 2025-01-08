package cool.scx.http.usagi.http2;

import cool.scx.http.web_socket.ScxServerWebSocket;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;

//todo 未完成
public class Http2ServerWebSocketHandshakeRequest extends Http2ServerRequest implements ScxServerWebSocketHandshakeRequest {

    @Override
    public ScxServerWebSocket acceptHandshake() {
        return null;
    }

    @Override
    public ScxServerWebSocket webSocket() {
        return null;
    }
}
