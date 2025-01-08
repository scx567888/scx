package cool.scx.http.usagi.web_socket;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.usagi.http1x.Http1xServerWebSocketHandshakeRequest;
import cool.scx.http.web_socket.ScxServerWebSocket;

/**
 * Usagi ServerWebSocket
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiServerWebSocket extends UsagiWebSocket implements ScxServerWebSocket {

    private final Http1xServerWebSocketHandshakeRequest handshakeRequest;

    public UsagiServerWebSocket(Http1xServerWebSocketHandshakeRequest handshakeRequest) {
        super(handshakeRequest.http1xConnection.tcpSocket, handshakeRequest.http1xConnection.dataReader, handshakeRequest.http1xConnection.dataWriter);
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
