package cool.scx.websocket.x;

import cool.scx.http.x.HttpClient;
import cool.scx.websocket.ScxClientWebSocketHandshakeRequest;
import cool.scx.websocket.ScxWebSocketClient;

public class WebSocketClient implements ScxWebSocketClient {

    private final HttpClient httpClient;

    private final WebSocketOptions options;

    public WebSocketClient(HttpClient httpClient, WebSocketOptions options) {
        this.httpClient = httpClient;
        this.options = options;
    }

    public WebSocketClient() {
        this.httpClient = new HttpClient();
        this.options = new WebSocketOptions();
    }

    @Override
    public ScxClientWebSocketHandshakeRequest webSocketHandshakeRequest() {
        return new ClientWebSocketHandshakeRequest(httpClient, options);
    }

    public HttpClient httpClient() {
        return httpClient;
    }

    public WebSocketOptions options() {
        return options;
    }

}
