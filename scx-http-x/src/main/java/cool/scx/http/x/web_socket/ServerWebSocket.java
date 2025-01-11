package cool.scx.http.x.web_socket;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.web_socket.ScxServerWebSocket;
import cool.scx.http.x.http1x.Http1xServerWebSocketHandshakeRequest;

/**
 * X ServerWebSocket
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ServerWebSocket extends WebSocket implements ScxServerWebSocket {

    private final Http1xServerWebSocketHandshakeRequest handshakeRequest;

    public ServerWebSocket(Http1xServerWebSocketHandshakeRequest handshakeRequest) {
        super(
                handshakeRequest.connection.tcpSocket,
                handshakeRequest.connection.dataReader,
                handshakeRequest.connection.dataWriter,
                handshakeRequest.connection.options.webSocketOptions()
        );
        this.handshakeRequest = handshakeRequest;
    }

    @Override
    public ScxURI uri() {
        return this.handshakeRequest.uri();
    }

    @Override
    public ScxHttpHeaders headers() {
        return this.handshakeRequest.headers();
    }

}
