package cool.scx.http.peach;

import cool.scx.http.*;
import cool.scx.http.uri.ScxURI;
import cool.scx.io.DataReader;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static cool.scx.http.WebSocketOpCode.*;
import static cool.scx.http.peach.WebSocketFrameHelper.readFrame;
import static cool.scx.http.peach.WebSocketFrameHelper.writeFrame;

public class PeachServerWebSocket implements ScxServerWebSocket {

    private final DataReader reader;
    private final OutputStream writer;
    private final ScxServerWebSocketHandshakeRequest handshakeServerRequest;
    private Consumer<String> textMessageHandler;
    private Consumer<byte[]> binaryMessageHandler;
    private Consumer<byte[]> pingHandler;
    private Consumer<byte[]> pongHandler;
    private BiConsumer<Integer, String> closeHandler;
    private Consumer<Throwable> errorHandler;
    private boolean isClosed;

    public PeachServerWebSocket(ScxServerWebSocketHandshakeRequest handshakeServerRequest, DataReader reader, OutputStream writer) {
        this.handshakeServerRequest = handshakeServerRequest;
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public ScxURI uri() {
        return this.handshakeServerRequest.uri();
    }

    @Override
    public ScxHttpHeaders headers() {
        return this.handshakeServerRequest.headers();
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
    public ScxWebSocket onClose(BiConsumer<Integer, String> closeHandler) {
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
        try {
            byte[] payload = textMessage.getBytes();
            sendFrame(payload, TEXT, last);
        } catch (Exception e) {
            if (errorHandler != null) {
                errorHandler.accept(e);
            }
        }
        return this;
    }

    @Override
    public ScxWebSocket send(byte[] binaryMessage, boolean last) {
        try {
            sendFrame(binaryMessage, BINARY, last);
        } catch (Exception e) {
            if (errorHandler != null) {
                errorHandler.accept(e);
            }
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
    public ScxWebSocket close(int code, String reason) {
        try {
            sendFrame(reason.getBytes(), WebSocketOpCode.CLOSE, true);
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
//        try {
//            scxTCPSocket.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        return this;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    public void start() {
        while (true) {
            try {
                var frame = readFrame(reader);
                handleFrame(frame);
            } catch (Exception e) {
                if (errorHandler != null) {
                    errorHandler.accept(e);
                }
                break; // 发生错误时退出循环
            }
        }
    }

    private void handleFrame(WebSocketFrame frame) {
        switch (frame.opCode()) {
            case CONTINUATION -> {

            }
            case TEXT -> {
                textMessageHandler.accept(new String(frame.payloadData()));
            }
            case BINARY -> {
                binaryMessageHandler.accept(frame.payloadData());
            }
            case CLOSE -> {
                isClosed = true;
                closeHandler.accept(0, new String(frame.payloadData()));
            }
            case PING -> {
                pingHandler.accept(frame.payloadData());
            }
            case PONG -> {
                pongHandler.accept(frame.payloadData());
            }
        }
    }

    private void sendFrame(byte[] payload, WebSocketOpCode opcode, boolean last) throws IOException {
        var f = new WebSocketFrame(last, opcode, payload);
        writeFrame(f, writer);
    }

}
