package cool.scx.http.x.web_socket;

import cool.scx.http.status.HttpStatusCode;
import cool.scx.http.ScxHttpBody;
import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.web_socket.ScxClientWebSocket;
import cool.scx.http.web_socket.ScxClientWebSocketHandshakeResponse;
import cool.scx.http.x.http1.Http1ClientConnection;

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
        return response.status() == HttpStatusCode.SWITCHING_PROTOCOLS;
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
    public HttpStatusCode status() {
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
