package cool.scx.websocket.x;

import cool.scx.http.x.HttpClient;
import cool.scx.websocket.ScxClientWebSocketHandshakeRequest;
import cool.scx.websocket.ScxWebSocketClient;

public class WebSocketClient implements ScxWebSocketClient {

    private final HttpClient httpClient;

    private final WebSocketOptions webSocketOptions;

    public WebSocketClient(HttpClient httpClient, WebSocketOptions webSocketOptions) {
        this.httpClient = httpClient;
        this.webSocketOptions = webSocketOptions;
    }

    public WebSocketClient() {
        this.httpClient = new HttpClient();
        this.webSocketOptions = new WebSocketOptions();
    }

    @Override
    public ScxClientWebSocketHandshakeRequest webSocketHandshakeRequest() {
        return new ClientWebSocketHandshakeRequest(httpClient, webSocketOptions);
    }

}
