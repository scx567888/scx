package cool.scx.websocket.x;

import cool.scx.http.x.XHttpClient;
import cool.scx.websocket.ScxClientWebSocketHandshakeRequest;
import cool.scx.websocket.ScxWebSocketClient;

public class XWebSocketClient implements ScxWebSocketClient {

    private XHttpClient httpClient;

    private WebSocketOptions webSocketOptions;
    
    

    @Override
    public ScxClientWebSocketHandshakeRequest webSocketHandshakeRequest() {
        return new XClientWebSocketHandshakeRequest(httpClient, webSocketOptions);
    }

}
