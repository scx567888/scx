package cool.scx.http.helidon;

import cool.scx.http.ScxClientWebSocket;
import io.helidon.websocket.WsSession;

/**
 * HelidonClientWebSocket
 */
class HelidonClientWebSocket extends HelidonWebSocket implements ScxClientWebSocket {

    private final HelidonClientWebSocketBuilder builder;

    public HelidonClientWebSocket(HelidonClientWebSocketBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void onOpen(WsSession session) {
        super.onOpen(session);
        if (builder.connectHandler != null) {
            builder.connectHandler.accept(this);
        }
    }

}
