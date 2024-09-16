package cool.scx.http.helidon;

import io.helidon.common.buffers.BufferData;
import io.helidon.http.Headers;
import io.helidon.http.HttpPrologue;
import io.helidon.websocket.WsListener;
import io.helidon.websocket.WsSession;
import io.helidon.websocket.WsUpgradeException;

import java.util.Optional;

class HelidonWebSocketRoute implements WsListener {

    private final HelidonHttpServer server;
    private HelidonServerWebSocket serverWebSocket;
    private HttpPrologue prologue;
    private Headers headers;

    public HelidonWebSocketRoute(HelidonHttpServer server) {
        this.server = server;
    }

    @Override
    public void onMessage(WsSession session, String text, boolean last) {
        if (serverWebSocket.textMessageHandler != null) {
            serverWebSocket.textMessageHandler.accept(text);
        }
    }

    @Override
    public void onMessage(WsSession session, BufferData buffer, boolean last) {
        if (serverWebSocket.binaryMessageHandler != null) {
            serverWebSocket.binaryMessageHandler.accept(buffer.readBytes());
        }
    }

    @Override
    public void onPing(WsSession session, BufferData buffer) {
        if (serverWebSocket.pingHandler != null) {
            serverWebSocket.pingHandler.accept(buffer.readBytes());
        }
    }

    @Override
    public void onPong(WsSession session, BufferData buffer) {
        if (serverWebSocket.pongHandler != null) {
            serverWebSocket.pongHandler.accept(buffer.readBytes());
        }
    }

    @Override
    public void onClose(WsSession session, int status, String reason) {
        if (serverWebSocket.closeHandler != null) {
            serverWebSocket.closeHandler.accept(status);
        }
    }

    @Override
    public void onError(WsSession session, Throwable t) {
        if (serverWebSocket.errorHandler != null) {
            serverWebSocket.errorHandler.accept(t);
        }
    }

    /**
     * onOpen 一定是先于其他方法执行的 所以这里 赋值没有问题
     */
    @Override
    public void onOpen(WsSession session) {
        this.serverWebSocket = new HelidonServerWebSocket(session, prologue, headers);
        if (this.server.webSocketHandler != null) {
            this.server.webSocketHandler.accept(serverWebSocket);
        }
    }

    /**
     * onHttpUpgrade 一定是先于 onOpen 执行的 所以这里 赋值没有问题
     */
    @Override
    public Optional<Headers> onHttpUpgrade(HttpPrologue prologue, Headers headers) throws WsUpgradeException {
        this.prologue = prologue;
        this.headers = headers;
        return WsListener.super.onHttpUpgrade(prologue, headers);
    }

}
