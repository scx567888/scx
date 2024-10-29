package cool.scx.http.peach;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.ScxServerWebSocket;
import cool.scx.http.ScxWebSocket;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.net.ScxTCPSocket;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PeachServerWebSocket implements ScxServerWebSocket {

    private final ScxURIWritable uri;
    private final ScxHttpHeadersWritable headers;
    private final ScxTCPSocket scxTCPSocket;
    private Consumer<String> textMessageHandler;
    private Consumer<byte[]> binaryMessageHandler;
    private Consumer<byte[]> pingHandler;
    private Consumer<byte[]> pongHandler;
    private BiConsumer<Integer, String> closeHandler;
    private Consumer<Throwable> errorHandler;

    public PeachServerWebSocket(HttpPrologue httpPrologue, ScxHttpHeadersWritable headers, ScxTCPSocket scxTCPSocket) {
        this.uri = ScxURI.of(httpPrologue.path());
        this.headers = headers;
        this.scxTCPSocket = scxTCPSocket;
    }

    @Override
    public ScxURI uri() {
        return null;
    }

    @Override
    public ScxHttpHeaders headers() {
        return null;
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
        return null;
    }

    @Override
    public ScxWebSocket send(byte[] binaryMessage, boolean last) {
        return null;
    }

    @Override
    public ScxWebSocket ping(byte[] data) {
        return null;
    }

    @Override
    public ScxWebSocket pong(byte[] data) {
        return null;
    }

    @Override
    public ScxWebSocket close(int code, String reason) {
        return null;
    }

    @Override
    public ScxWebSocket terminate() {
        return null;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    void start() {
        var inputStream = scxTCPSocket.inputStream();
        while (true) {
            try {
                var frame = new WebSocketFrame(inputStream);
                handleFrame(frame, scxTCPSocket);
            } catch (Exception e) {
                if (errorHandler != null) {
                    errorHandler.accept(e);
                }
                break; // 发生错误时退出循环
            }
        }
    }

    private void handleFrame(WebSocketFrame frame, ScxTCPSocket scxTCPSocket) {
        switch (frame.getOpcode()) {
            case 0x0 -> {

            }
            case 0x1 -> {
                textMessageHandler.accept(new String(frame.getPayloadData()));
            }
            case 0x2 -> {
            }
            case 0x8 -> {
            }
            case 0x9 -> {
            }
            case 0xA -> {
            }
        }
    }

}
