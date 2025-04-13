package cool.scx.websocket.x;

import cool.scx.http.x.XHttpClient;
import cool.scx.websocket.ScxClientWebSocketHandshakeRequest;
import cool.scx.websocket.ScxWebSocketClient;

public class XWebSocketClient implements ScxWebSocketClient {

    private final XHttpClient httpClient;

    private final WebSocketOptions webSocketOptions;

    public XWebSocketClient(XHttpClient httpClient, WebSocketOptions webSocketOptions) {
        this.httpClient = httpClient;
        this.webSocketOptions = webSocketOptions;
    }

    public XWebSocketClient() {
        this.httpClient = new XHttpClient();
        this.webSocketOptions = new WebSocketOptions();
    }

    @Override
    public ScxClientWebSocketHandshakeRequest webSocketHandshakeRequest() {
        return new XClientWebSocketHandshakeRequest(httpClient, webSocketOptions);
    }

}
