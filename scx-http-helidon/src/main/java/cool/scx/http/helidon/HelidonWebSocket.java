package cool.scx.http.helidon;

import cool.scx.http.ScxWebSocket;
import io.helidon.common.buffers.BufferData;
import io.helidon.http.Headers;
import io.helidon.http.HttpPrologue;
import io.helidon.websocket.WsListener;
import io.helidon.websocket.WsSession;
import io.helidon.websocket.WsUpgradeException;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class HelidonWebSocket implements ScxWebSocket, WsListener {

    protected HttpPrologue prologue;
    protected Headers headers;
    protected WsSession wsSession;
    private Consumer<String> textMessageHandler;
    private Consumer<byte[]> binaryMessageHandler;
    private Consumer<byte[]> pingHandler;
    private Consumer<byte[]> pongHandler;
    private BiConsumer<Integer, String> closeHandler;
    private Consumer<Throwable> errorHandler;


    @Override
    public HelidonWebSocket onTextMessage(Consumer<String> textMessageHandler) {
        this.textMessageHandler = textMessageHandler;
        return this;
    }

    @Override
    public HelidonWebSocket onBinaryMessage(Consumer<byte[]> binaryMessageHandler) {
        this.binaryMessageHandler = binaryMessageHandler;
        return this;
    }

    @Override
    public HelidonWebSocket onPing(Consumer<byte[]> pingHandler) {
        this.pingHandler = pingHandler;
        return this;
    }

    @Override
    public HelidonWebSocket onPong(Consumer<byte[]> pongHandler) {
        this.pongHandler = pongHandler;
        return this;
    }

    @Override
    public HelidonWebSocket onClose(BiConsumer<Integer, String> closeHandler) {
        this.closeHandler = closeHandler;
        return this;
    }

    @Override
    public HelidonWebSocket onError(Consumer<Throwable> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    @Override
    public ScxWebSocket send(String textMessage, boolean var2) {
        if (wsSession == null) {
            throw new IllegalStateException("wsSession is null");
        }
        wsSession.send(textMessage, var2);
        return this;
    }

    @Override
    public ScxWebSocket send(byte[] binaryMessage, boolean var2) {
        if (wsSession == null) {
            throw new IllegalStateException("wsSession is null");
        }
        wsSession.send(BufferData.create(binaryMessage), var2);
        return this;
    }


    @Override
    public ScxWebSocket ping(byte[] data) {
        if (wsSession == null) {
            throw new IllegalStateException("wsSession is null");
        }
        wsSession.ping(BufferData.create(data));
        return this;
    }

    @Override
    public ScxWebSocket pong(byte[] data) {
        if (wsSession == null) {
            throw new IllegalStateException("wsSession is null");
        }
        wsSession.pong(BufferData.create(data));
        return this;
    }

    @Override
    public ScxWebSocket close(int var1, String var2) {
        if (wsSession == null) {
            throw new IllegalStateException("wsSession is null");
        }
        wsSession.close(var1, var2);
        return this;
    }

    @Override
    public ScxWebSocket terminate() {
        if (wsSession == null) {
            throw new IllegalStateException("wsSession is null");
        }
        wsSession.terminate();
        return this;
    }

    @Override
    public boolean isClosed() {
        //todo 
        return false;
    }

    @Override
    public void onMessage(WsSession session, String text, boolean last) {
        if (textMessageHandler != null) {
            textMessageHandler.accept(text);
        }
    }

    @Override
    public void onMessage(WsSession session, BufferData buffer, boolean last) {
        if (binaryMessageHandler != null) {
            binaryMessageHandler.accept(buffer.readBytes());
        }
    }

    @Override
    public void onPing(WsSession session, BufferData buffer) {
        if (pingHandler != null) {
            pingHandler.accept(buffer.readBytes());
        }
    }

    @Override
    public void onPong(WsSession session, BufferData buffer) {
        if (pongHandler != null) {
            pongHandler.accept(buffer.readBytes());
        }
    }

    @Override
    public void onClose(WsSession session, int status, String reason) {
        if (closeHandler != null) {
            closeHandler.accept(status, reason);
        }
    }

    @Override
    public void onError(WsSession session, Throwable t) {
        if (errorHandler != null) {
            errorHandler.accept(t);
        }
    }

    @Override
    public void onOpen(WsSession session) {
        this.wsSession = session;
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
