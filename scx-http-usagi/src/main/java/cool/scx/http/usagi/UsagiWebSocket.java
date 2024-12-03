package cool.scx.http.usagi;

import cool.scx.http.web_socket.ScxWebSocket;
import cool.scx.http.web_socket.ScxWebSocketCloseInfo;
import cool.scx.http.web_socket.WebSocketFrame;
import cool.scx.http.web_socket.WebSocketOpCode;
import cool.scx.io.DataReader;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import static cool.scx.http.web_socket.WebSocketCloseInfo.NORMAL_CLOSE;
import static cool.scx.http.web_socket.WebSocketFrameHelper.*;
import static cool.scx.http.web_socket.WebSocketOpCode.*;

public class UsagiWebSocket implements ScxWebSocket {

    protected final DataReader reader;
    protected final OutputStream writer;

    //为了防止底层的 OutputStream 被乱序写入 此处需要加锁
    protected final ReentrantLock lock;
    private final ScxTCPSocket tcpSocket;

    protected Consumer<String> textMessageHandler;
    protected Consumer<byte[]> binaryMessageHandler;
    protected Consumer<byte[]> pingHandler;
    protected Consumer<byte[]> pongHandler;
    protected Consumer<ScxWebSocketCloseInfo> closeHandler;
    protected Consumer<Throwable> errorHandler;
    protected boolean isClosed;

    public UsagiWebSocket(ScxTCPSocket tcpSocket, DataReader reader, OutputStream writer) {
        this.tcpSocket = tcpSocket;
        this.reader = reader;
        this.writer = writer;
        this.lock = new ReentrantLock();
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

    @Override
    public ScxWebSocket send(String textMessage, boolean last) {
        byte[] payload = textMessage.getBytes();
        try {
            sendFrame(payload, TEXT, last);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ScxWebSocket send(byte[] binaryMessage, boolean last) {
        try {
            sendFrame(binaryMessage, BINARY, last);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ScxWebSocket ping(byte[] data) {
        try {
            sendFrame(data, PING, true);
        } catch (Exception e) {
            if (errorHandler != null) {
                errorHandler.accept(e);
            }
        }
        return this;
    }

    @Override
    public ScxWebSocket pong(byte[] data) {
        try {
            sendFrame(data, WebSocketOpCode.PONG, true);
        } catch (Exception e) {
            if (errorHandler != null) {
                errorHandler.accept(e);
            }
        }
        return this;
    }

    @Override
    public ScxWebSocket close(ScxWebSocketCloseInfo closeInfo) {
        if (isClosed) {
            return this;
        }
        isClosed = true;
        try {
            var closePayload = createClosePayload(closeInfo);
            sendFrame(closePayload, WebSocketOpCode.CLOSE, true);
        } catch (Exception e) {
            if (errorHandler != null) {
                errorHandler.accept(e);
            }
        }
        return this;
    }

    @Override
    public ScxWebSocket terminate() {
        isClosed = true;
        //todo 这里需要关闭 tcp 连接 ?
        try {
            tcpSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    public void start() {
        while (true) {
            try {
                var frame = readFrameUntilLast(reader);
                handleFrame(frame);
            } catch (Exception e) {
                if (errorHandler != null) {
//                    errorHandler.accept(e);
                }
                break;
            }
        }
    }

    public void handleFrame(WebSocketFrame frame) {
        switch (frame.opCode()) {
            case CONTINUATION -> {
                // 理论上不会走到这里
            }
            case TEXT -> _callOnTextMessage(frame);
            case BINARY -> _callOnBinaryMessage(frame);
            case CLOSE -> _handleClose(frame);
            case PING -> {
                pingHandler.accept(frame.payloadData());
            }
            case PONG -> {
                pongHandler.accept(frame.payloadData());
            }
        }
    }

    public void sendFrame(byte[] payload, WebSocketOpCode opcode, boolean last) throws IOException {
        lock.lock();
        try {
            var f = WebSocketFrame.of(last, opcode, payload);
            writeFrame(f, writer);
        } finally {
            lock.unlock();
        }
    }

    public void _handleClose(WebSocketFrame frame) {
        close(NORMAL_CLOSE);
        try {
            tcpSocket.close();
        } catch (IOException e) {
            _callOnError(e);
        }
        _callOnClose(frame);
    }

    public void _callOnTextMessage(WebSocketFrame frame) {
        if (textMessageHandler != null) {
            textMessageHandler.accept(new String(frame.payloadData()));
        }
    }

    public void _callOnBinaryMessage(WebSocketFrame frame) {
        if (binaryMessageHandler != null) {
            binaryMessageHandler.accept(frame.payloadData());
        }
    }

    public void _callOnClose(WebSocketFrame frame) {
        if (closeHandler != null) {
            closeHandler.accept(parseCloseInfo(frame.payloadData()));
        }
    }

    private void _callOnError(Exception e) {
        if (errorHandler != null) {
            errorHandler.accept(e);
        }
    }

}
