package cool.scx.websocket.x;

import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.x.http1.Http1ServerConnection;
import cool.scx.http.x.http1.Http1UpgradeHandler;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.headers.upgrade.ScxUpgrade;
import cool.scx.http.x.http1.request_line.Http1RequestLine;
import cool.scx.io.ByteInput;

import static cool.scx.http.x.http1.headers.upgrade.Upgrade.WEB_SOCKET;

public class WebSocketUpgradeHandler implements Http1UpgradeHandler<Http1ServerWebSocketHandshakeRequest> {

    private final WebSocketOptions webSocketOptions;

    public WebSocketUpgradeHandler(WebSocketOptions webSocketOptions) {
        this.webSocketOptions = webSocketOptions;
    }

    public WebSocketUpgradeHandler() {
        this.webSocketOptions = new WebSocketOptions();
    }

    @Override
    public boolean canUpgrade(ScxUpgrade scxUpgrade) {
        return scxUpgrade == WEB_SOCKET;
    }

    @Override
    public Http1ServerWebSocketHandshakeRequest createUpgradedRequest(Http1ServerConnection connection, Http1RequestLine requestLine, Http1Headers headers, ByteInput bodyByteInput) {
        return new Http1ServerWebSocketHandshakeRequest(connection, requestLine, headers, bodyByteInput, webSocketOptions);
    }

}
