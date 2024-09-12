package cool.scx.http.helidon;

import cool.scx.http.ScxServerWebSocket;
import io.helidon.common.buffers.BufferData;
import io.helidon.websocket.WsSession;

import java.util.function.Consumer;

class HelidonServerWebSocket implements ScxServerWebSocket {

    private final WsSession wsSession;
    Consumer<String> textMessageHandler;
    Consumer<byte[]> binaryMessageHandler;
    Consumer<byte[]> pingHandler;
    Consumer<byte[]> pongHandler;
    Consumer<Integer> closeHandler;
    Consumer<Throwable> errorHandler;

    public HelidonServerWebSocket(WsSession session) {
        this.wsSession = session;
    }


    @Override
    public ScxServerWebSocket onTextMessage(Consumer<String> textMessageHandler) {
        this.textMessageHandler = textMessageHandler;
        return this;
    }

    @Override
    public ScxServerWebSocket onBinaryMessage(Consumer<byte[]> binaryMessageHandler) {
        this.binaryMessageHandler = binaryMessageHandler;
        return this;
    }

    @Override
    public ScxServerWebSocket onPing(Consumer<byte[]> pingHandler) {
        this.pingHandler = pingHandler;
        return this;
    }

    @Override
    public ScxServerWebSocket onPong(Consumer<byte[]> pongHandler) {
        this.pongHandler = pongHandler;
        return this;
    }

    @Override
    public ScxServerWebSocket onClose(Consumer<Integer> closeHandler) {
        this.closeHandler = closeHandler;
        return this;
    }

    @Override
    public ScxServerWebSocket onError(Consumer<Throwable> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    @Override
    public ScxServerWebSocket send(String textMessage, boolean var2) {
        wsSession.send(textMessage, var2);
        return this;
    }

    @Override
    public ScxServerWebSocket send(byte[] binaryMessage, boolean var2) {
        wsSession.send(BufferData.create(binaryMessage), var2);
        return this;
    }

    @Override
    public ScxServerWebSocket ping(byte[] data) {
        wsSession.ping(BufferData.create(data));
        return this;
    }

    @Override
    public ScxServerWebSocket pong(byte[] data) {
        wsSession.pong(BufferData.create(data));
        return this;
    }

    @Override
    public ScxServerWebSocket close(int var1, String var2) {
        wsSession.close(var1, var2);
        return this;
    }

}
