package cool.scx.websocket.x;

import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.x.http1.Http1ServerConnection;
import cool.scx.http.x.http1.Http1UpgradeHandler;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.headers.upgrade.ScxUpgrade;
import cool.scx.http.x.http1.request_line.Http1RequestLine;

import java.io.InputStream;

import static cool.scx.http.x.http1.headers.upgrade.Upgrade.WEB_SOCKET;

public class WebSocketUpgradeHandler implements Http1UpgradeHandler {

    private final WebSocketOptions webSocketOptions;

    public WebSocketUpgradeHandler(WebSocketOptions webSocketOptions) {
        this.webSocketOptions = webSocketOptions;
    }

    @Override
    public boolean canHandle(ScxUpgrade scxUpgrade) {
        return scxUpgrade == WEB_SOCKET;
    }

    @Override
    public ScxHttpServerRequest createScxHttpServerRequest(Http1ServerConnection connection, Http1RequestLine requestLine, Http1Headers headers, InputStream bodyInputStream) {
        return new Http1ServerWebSocketHandshakeRequest(connection, requestLine, headers, bodyInputStream,webSocketOptions);
    }

}
