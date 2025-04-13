package cool.scx.websocket.x;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.uri.ScxURI;
import cool.scx.io.data_reader.DataReader;
import cool.scx.tcp.ScxTCPSocket;
import cool.scx.websocket.ScxServerWebSocket;

import java.io.OutputStream;

/// X ServerWebSocket
///
/// @author scx567888
/// @version 0.0.1
public class ServerWebSocket extends WebSocket implements ScxServerWebSocket {

    private final Http1ServerWebSocketHandshakeRequest handshakeRequest;

    public ServerWebSocket(ScxTCPSocket tcpSocket, DataReader reader, OutputStream writer, WebSocketOptions options, Http1ServerWebSocketHandshakeRequest handshakeRequest) {
        super(tcpSocket, reader, writer, options);
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
