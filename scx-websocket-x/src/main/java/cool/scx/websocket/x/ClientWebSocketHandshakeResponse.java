package cool.scx.websocket.x;

import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.body.ScxHttpBody;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.status_code.ScxHttpStatusCode;
import cool.scx.http.version.HttpVersion;
import cool.scx.http.x.http1.Http1ClientConnection;
import cool.scx.websocket.ScxClientWebSocketHandshakeResponse;
import cool.scx.websocket.ScxWebSocket;

import static cool.scx.http.status_code.HttpStatusCode.SWITCHING_PROTOCOLS;
import static cool.scx.http.version.HttpVersion.HTTP_1_1;

public class ClientWebSocketHandshakeResponse implements ScxClientWebSocketHandshakeResponse {

    private final Http1ClientConnection connection;
    private final ScxHttpClientResponse response;
    private final WebSocketOptions webSocketOptions;
    private ScxWebSocket webSocket;

    public ClientWebSocketHandshakeResponse(Http1ClientConnection connection, ScxHttpClientResponse response, WebSocketOptions webSocketOptions) {
        this.connection = connection;
        this.response = response;
        this.webSocketOptions = webSocketOptions;
    }

    @Override
    public boolean handshakeSucceeded() {
        return SWITCHING_PROTOCOLS.equals(response.statusCode());
    }

    @Override
    public ScxWebSocket webSocket() {
        if (webSocket == null) {
            if (!handshakeSucceeded()) {
                throw new RuntimeException("Unexpected response status: " + response.statusCode());
            }
            webSocket = new WebSocket(connection.tcpSocket, connection.dataReader, connection.dataWriter, webSocketOptions, true);
        }
        return webSocket;
    }

    @Override
    public ScxHttpStatusCode statusCode() {
        return response.statusCode();
    }

    @Override
    public HttpVersion version() {
        return HTTP_1_1;
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
