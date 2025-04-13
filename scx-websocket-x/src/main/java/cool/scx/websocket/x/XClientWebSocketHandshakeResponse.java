package cool.scx.websocket.x;

import cool.scx.http.ScxHttpBody;
import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.status.ScxHttpStatus;
import cool.scx.http.x.http1.Http1ClientConnection;
import cool.scx.websocket.ScxClientWebSocket;
import cool.scx.websocket.ScxClientWebSocketHandshakeResponse;

import static cool.scx.http.status.HttpStatus.SWITCHING_PROTOCOLS;

public class XClientWebSocketHandshakeResponse implements ScxClientWebSocketHandshakeResponse {

    private final Http1ClientConnection connection;
    private final ScxHttpClientResponse response;
    private final WebSocketOptions webSocketOptions;
    private ScxClientWebSocket webSocket;

    public XClientWebSocketHandshakeResponse(Http1ClientConnection connection, ScxHttpClientResponse response, WebSocketOptions webSocketOptions) {
        this.connection = connection;
        this.response = response;
        this.webSocketOptions = webSocketOptions;
    }

    @Override
    public boolean handshakeSucceeded() {
        return SWITCHING_PROTOCOLS.equals(response.status());
    }

    @Override
    public ScxClientWebSocket webSocket() {
        if (webSocket == null) {
            if (!handshakeSucceeded()) {
                throw new RuntimeException("Unexpected response status: " + response.status());
            }
            webSocket = new ClientWebSocket(connection.tcpSocket, connection.dataReader, connection.tcpSocket.outputStream(), webSocketOptions);
        }
        return webSocket;
    }

    @Override
    public ScxHttpStatus status() {
        return response.status();
    }

    @Override
    public ScxHttpHeaders headers() {
        return response.headers();
    }

    @Override
    public ScxHttpBody body() {
        return response.body();
    }

}
