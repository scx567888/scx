package cool.scx.http;

import java.util.function.Consumer;

public interface ScxServerWebSocket {

    ScxServerWebSocket onTextMessage(Consumer<String> textMessageHandler);

    ScxServerWebSocket onBinaryMessage(Consumer<byte[]> binaryMessageHandler);

    ScxServerWebSocket onPing(Consumer<byte[]> pingHandler);

    ScxServerWebSocket onPong(Consumer<byte[]> pongHandler);

    ScxServerWebSocket onClose(Consumer<Integer> closeHandler);

    ScxServerWebSocket onError(Consumer<Throwable> errorHandler);

    ScxServerWebSocket send(String textMessage, boolean var2);

    ScxServerWebSocket send(byte[] binaryMessage, boolean var2);

    ScxServerWebSocket ping(byte[] data);

    ScxServerWebSocket pong(byte[] data);

    ScxServerWebSocket close(int var1, String var2);

}
