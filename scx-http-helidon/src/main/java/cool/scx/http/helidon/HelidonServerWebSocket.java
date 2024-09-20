package cool.scx.http.helidon;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxServerWebSocket;
import cool.scx.http.uri.ScxURI;
import io.helidon.websocket.WsSession;

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
        return ScxURI.of()
                .path(prologue.uriPath().path())
                .query(new HelidonURIQuery(prologue.query()))
                .fragment(prologue.fragment().hasValue() ? prologue.fragment().value() : null);
    }

    @Override
    public ScxHttpHeaders headers() {
        return new HelidonHttpHeaders<>(headers);
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
