package cool.scx.http.usagi.web_socket;

import cool.scx.http.web_socket.ScxWebSocket;
import cool.scx.http.web_socket.WebSocketOpCode;
import cool.scx.http.web_socket.handler.BinaryMessageHandler;
import cool.scx.http.web_socket.handler.CloseHandler;
import cool.scx.http.web_socket.handler.TextMessageHandler;

import java.util.function.Consumer;

import static cool.scx.http.usagi.web_socket.WebSocketFrameHelper.createClosePayload;
import static cool.scx.http.web_socket.WebSocketOpCode.*;

// 实现一些最基本的方法  
public abstract class AbstractWebSocket implements ScxWebSocket {

    protected TextMessageHandler textMessageHandler;
    protected BinaryMessageHandler binaryMessageHandler;
    protected Consumer<byte[]> pingHandler;
    protected Consumer<byte[]> pongHandler;
    protected CloseHandler closeHandler;
    protected Consumer<Throwable> errorHandler;

    public AbstractWebSocket() {
        this.textMessageHandler = null;
        this.binaryMessageHandler = null;
        this.pingHandler = null;
        this.pongHandler = null;
        this.closeHandler = null;
        this.errorHandler = null;
    }

    @Override
    public ScxWebSocket onTextMessage(TextMessageHandler textMessageHandler) {
        this.textMessageHandler = textMessageHandler;
        return this;
    }

    @Override
    public ScxWebSocket onBinaryMessage(BinaryMessageHandler binaryMessageHandler) {
        this.binaryMessageHandler = binaryMessageHandler;
        return this;
    }

    @Override
    public ScxWebSocket onPing(Consumer<byte[]> pingHandler) {
        this.pingHandler = pingHandler;
        return this;
    }

    @Override
    public ScxWebSocket onPong(Consumer<byte[]> pongHandler) {
        this.pongHandler = pongHandler;
        return this;
    }

    @Override
    public ScxWebSocket onClose(CloseHandler closeHandler) {
        this.closeHandler = closeHandler;
        return this;
    }

    @Override
    public ScxWebSocket onError(Consumer<Throwable> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    protected void _callOnTextMessage(String text, boolean last) {
        if (textMessageHandler != null) {
            textMessageHandler.handle(text, last);
        }
    }

    protected void _callOnBinaryMessage(byte[] binary, boolean last) {
        if (binaryMessageHandler != null) {
            binaryMessageHandler.handle(binary, last);
        }
    }

    protected void _callOnPing(byte[] bytes) {
        if (pingHandler != null) {
            pingHandler.accept(bytes);
        }
    }

    protected void _callOnPong(byte[] bytes) {
        if (pongHandler != null) {
            pongHandler.accept(bytes);
        }
    }

    protected void _callOnClose(int code, String reason) {
        if (closeHandler != null) {
            closeHandler.handle(code, reason);
        }
    }

    protected void _callOnError(Exception e) {
        if (errorHandler != null) {
            errorHandler.accept(e);
        }
    }

    @Override
    public ScxWebSocket send(String textMessage, boolean last) {
        var payload = textMessage != null ? textMessage.getBytes() : new byte[]{};
        sendFrame(TEXT, payload, last);
        return this;
    }

    @Override
    public ScxWebSocket send(byte[] binaryMessage, boolean last) {
        sendFrame(BINARY, binaryMessage, last);
        return this;
    }

    @Override
    public ScxWebSocket ping(byte[] data) {
        sendFrame(PING, data, true);
        return this;
    }

    @Override
    public ScxWebSocket pong(byte[] data) {
        sendFrame(PONG, data, true);
        return this;
    }

    @Override
    public ScxWebSocket close(int code, String reason) {
        var closePayload = createClosePayload(code, reason);
        sendFrame(CLOSE, closePayload, true);
        return this;
    }

    protected abstract void sendFrame(WebSocketOpCode opcode, byte[] payload, boolean last);

}
