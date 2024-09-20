package cool.scx.http.helidon;

import cool.scx.http.ScxClientWebSocket;
import cool.scx.http.ScxClientWebSocketBuilder;
import cool.scx.http.uri.ScxURI;
import io.helidon.webclient.websocket.WsClient;
import io.helidon.websocket.WsSession;

import java.util.function.Consumer;

/**
 * HelidonClientWebSocket
 */
class HelidonClientWebSocket extends HelidonWebSocket implements ScxClientWebSocket, ScxClientWebSocketBuilder {

    private final WsClient wsClient;
    private Consumer<ScxClientWebSocket> connectHandler;
    private ScxURI uri;

    public HelidonClientWebSocket(WsClient wsClient) {
        this.wsClient = wsClient;
    }

    @Override
    public ScxURI uri() {
        return uri;
    }

    @Override
    public HelidonClientWebSocket uri(ScxURI uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public void onOpen(WsSession session) {
        super.onOpen(session);
        if (connectHandler != null) {
            connectHandler.accept(this);
        }
    }

    @Override
    public HelidonClientWebSocket onConnect(Consumer<ScxClientWebSocket> connectHandler) {
        this.connectHandler = connectHandler;
        return this;
    }

    @Override
    public void connect() {
        wsClient.connect(uri.toString(), this);
    }

}
