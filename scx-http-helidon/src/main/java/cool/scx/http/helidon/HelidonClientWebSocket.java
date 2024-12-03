package cool.scx.http.helidon;

import cool.scx.http.web_socket.ScxClientWebSocket;
import io.helidon.websocket.WsSession;

/**
 * HelidonClientWebSocket
 *
 * @author scx567888
 * @version 0.0.1
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
