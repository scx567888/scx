package cool.scx.http.usagi.web_socket;

import cool.scx.http.web_socket.ScxWebSocket;
import cool.scx.http.web_socket.ScxWebSocketCloseInfo;
import cool.scx.http.web_socket.WebSocketOpCode;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.System.Logger;
import java.util.function.Consumer;

import static cool.scx.http.usagi.web_socket.WebSocketFrameHelper.createClosePayload;
import static cool.scx.http.usagi.web_socket.WebSocketFrameHelper.parseCloseInfo;
import static cool.scx.http.web_socket.WebSocketOpCode.*;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.getLogger;

// 实现一些最基本的方法  
public abstract class AbstractWebSocket implements ScxWebSocket {

    public static final Logger LOGGER = getLogger(AbstractWebSocket.class.getName());

    //限制只发送一次 close 帧
    protected boolean closeFrameSent;

    protected Consumer<String> textMessageHandler;
    protected Consumer<byte[]> binaryMessageHandler;
    protected Consumer<byte[]> pingHandler;
    protected Consumer<byte[]> pongHandler;
    protected Consumer<ScxWebSocketCloseInfo> closeHandler;
    protected Consumer<Throwable> errorHandler;

    public AbstractWebSocket() {
        closeFrameSent = false;
        textMessageHandler = null;
        binaryMessageHandler = null;
        pingHandler = null;
        pongHandler = null;
        closeHandler = null;
        errorHandler = null;
    }

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
            try {
                textMessageHandler.accept(new String(frame.payloadData()));
            } catch (Exception e) {
                _handleCallbackError(e);
            }
        }
    }

    protected void _callOnBinaryMessage(WebSocketFrame frame) {
        if (binaryMessageHandler != null) {
            try {
                binaryMessageHandler.accept(frame.payloadData());
            } catch (Exception e) {
                _handleCallbackError(e);
            }
        }
    }

    protected void _callOnPing(WebSocketFrame frame) {
        if (pingHandler != null) {
            try {
                pingHandler.accept(frame.payloadData());
            } catch (Exception e) {
                _handleCallbackError(e);
            }
        }
    }

    protected void _callOnPong(WebSocketFrame frame) {
        if (pongHandler != null) {
            try {
                pongHandler.accept(frame.payloadData());
            } catch (Exception e) {
                _handleCallbackError(e);
            }
        }
    }

    protected void _callOnClose(WebSocketFrame frame) {
        if (closeHandler != null) {
            try {
                closeHandler.accept(parseCloseInfo(frame.payloadData()));
            } catch (Exception e) {
                _handleCallbackError(e);
            }
        }
    }

    protected void _callOnError(Exception e) {
        if (errorHandler != null) {
            try {
                errorHandler.accept(e);
            } catch (Exception ex) {
                _handleCallbackError(ex);
            }
        }
    }

    @Override
    public ScxWebSocket send(String textMessage, boolean last) {
        var payload = textMessage.getBytes();
        try {
            sendFrame(TEXT, payload, last);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ScxWebSocket send(byte[] binaryMessage, boolean last) {
        try {
            sendFrame(BINARY, binaryMessage, last);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ScxWebSocket ping(byte[] data) {
        try {
            sendFrame(PING, data, true);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ScxWebSocket pong(byte[] data) {
        try {
            sendFrame(PONG, data, true);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ScxWebSocket close(ScxWebSocketCloseInfo closeInfo) {
        // close 帧只允许发送一次
        if (closeFrameSent){
            return this;
        }
        var closePayload = createClosePayload(closeInfo);
        try {
            sendFrame(WebSocketOpCode.CLOSE, closePayload, true);
            //重置 close 帧发送标识
            closeFrameSent = true;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    public abstract void sendFrame(WebSocketOpCode opcode, byte[] payload, boolean last) throws IOException;

    protected void _handleCallbackError(Exception e) {
        // 记录错误日志
        LOGGER.log(ERROR, "Error during callback execution: ", e);
    }

}
