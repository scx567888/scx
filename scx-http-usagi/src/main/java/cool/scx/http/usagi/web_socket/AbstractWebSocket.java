package cool.scx.http.usagi.web_socket;

import cool.scx.http.web_socket.ScxWebSocket;
import cool.scx.http.web_socket.ScxWebSocketCloseInfo;

import java.util.function.Consumer;

import static cool.scx.http.usagi.web_socket.WebSocketFrameHelper.parseCloseInfo;

// 实现一些最基本的方法  
public abstract class AbstractWebSocket implements ScxWebSocket {
    
    protected Consumer<String> textMessageHandler;
    protected Consumer<byte[]> binaryMessageHandler;
    protected Consumer<byte[]> pingHandler;
    protected Consumer<byte[]> pongHandler;
    protected Consumer<ScxWebSocketCloseInfo> closeHandler;
    protected Consumer<Throwable> errorHandler;

    @Override
    public ScxWebSocket onTextMessage(Consumer<String> textMessageHandler) {
        this.textMessageHandler = textMessageHandler;
        return this;
    }

    @Override
    public ScxWebSocket onBinaryMessage(Consumer<byte[]> binaryMessageHandler) {
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
    public ScxWebSocket onClose(Consumer<ScxWebSocketCloseInfo> closeHandler) {
        this.closeHandler = closeHandler;
        return this;
    }

    @Override
    public ScxWebSocket onError(Consumer<Throwable> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    protected void _callOnTextMessage(WebSocketFrame frame) {
        if (textMessageHandler != null) {
            textMessageHandler.accept(new String(frame.payloadData()));
        }
    }

    protected void _callOnBinaryMessage(WebSocketFrame frame) {
        if (binaryMessageHandler != null) {
            binaryMessageHandler.accept(frame.payloadData());
        }
    }

    protected void _callOnPing(WebSocketFrame frame) {
        if (pingHandler != null) {
            pingHandler.accept(frame.payloadData());
        }
    }

    protected void _callOnPong(WebSocketFrame frame) {
        if (pongHandler != null) {
            pongHandler.accept(frame.payloadData());
        }
    }

    protected void _callOnClose(WebSocketFrame frame) {
        if (closeHandler != null) {
            closeHandler.accept(parseCloseInfo(frame.payloadData()));
        }
    }

    protected void _callOnError(Exception e) {
        if (errorHandler != null) {
            errorHandler.accept(e);
        }
    }
    
}
