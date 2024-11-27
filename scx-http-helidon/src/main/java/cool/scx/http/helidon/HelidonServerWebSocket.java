package cool.scx.http.helidon;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.web_socket.ScxServerWebSocket;
import io.helidon.websocket.WsSession;

import static cool.scx.http.helidon.HelidonHelper.convertHeaders;
import static cool.scx.http.helidon.HelidonHelper.createScxURI;

/**
 * HelidonServerWebSocket
 */
class HelidonServerWebSocket extends HelidonWebSocket implements ScxServerWebSocket {

    private final HelidonHttpServer server;

    public HelidonServerWebSocket(HelidonHttpServer server) {
        this.server = server;
    }

    @Override
    public ScxURI uri() {
        return createScxURI(prologue);
    }

    @Override
    public ScxHttpHeaders headers() {
        return convertHeaders(headers);
    }

    /**
     * onOpen 一定是先于其他方法执行的 所以这里 赋值没有问题
     */
    @Override
    public void onOpen(WsSession session) {
        super.onOpen(session);
        if (this.server.webSocketHandler != null) {
            this.server.webSocketHandler.accept(this);
        }
    }

}
