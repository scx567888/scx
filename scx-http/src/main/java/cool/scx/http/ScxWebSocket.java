package cool.scx.http;

import java.util.function.Consumer;

/**
 * ScxWebSocket
 */
public interface ScxWebSocket {

    ScxWebSocket onTextMessage(Consumer<String> textMessageHandler);

    ScxWebSocket onBinaryMessage(Consumer<byte[]> binaryMessageHandler);

    ScxWebSocket onPing(Consumer<byte[]> pingHandler);

    ScxWebSocket onPong(Consumer<byte[]> pongHandler);

    ScxWebSocket onClose(Consumer<Integer> closeHandler);

    ScxWebSocket onError(Consumer<Throwable> errorHandler);

    ScxWebSocket send(String textMessage, boolean var2);

    ScxWebSocket send(byte[] binaryMessage, boolean var2);

    ScxWebSocket ping(byte[] data);

    ScxWebSocket pong(byte[] data);

    ScxWebSocket close(int var1, String var2);
    
    default ScxWebSocket send(String textMessage) {
        return send(textMessage, false);
    }
    
    default ScxWebSocket send(byte[] binaryMessage) {
        return send(binaryMessage, false);
    }
    
}