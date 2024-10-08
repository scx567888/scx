package cool.scx.http;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static cool.scx.http.WebSocketCloseCode.NORMAL_CLOSE;

/**
 * ScxWebSocket
 */
public interface ScxWebSocket {

    ScxWebSocket onTextMessage(Consumer<String> textMessageHandler);

    ScxWebSocket onBinaryMessage(Consumer<byte[]> binaryMessageHandler);

    ScxWebSocket onPing(Consumer<byte[]> pingHandler);

    ScxWebSocket onPong(Consumer<byte[]> pongHandler);

    ScxWebSocket onClose(BiConsumer<Integer, String> closeHandler);

    ScxWebSocket onError(Consumer<Throwable> errorHandler);

    ScxWebSocket send(String textMessage, boolean last);

    ScxWebSocket send(byte[] binaryMessage, boolean last);

    ScxWebSocket ping(byte[] data);

    ScxWebSocket pong(byte[] data);

    ScxWebSocket close(int code, String reason);

    ScxWebSocket terminate();

    boolean isClosed();

    default ScxWebSocket send(String textMessage) {
        return send(textMessage, true);
    }

    default ScxWebSocket send(byte[] binaryMessage) {
        return send(binaryMessage, true);
    }

    default ScxWebSocket close(WebSocketCloseCode closeCode) {
        return close(closeCode.code(), closeCode.reason());
    }

    default ScxWebSocket close() {
        return close(NORMAL_CLOSE);
    }

}
