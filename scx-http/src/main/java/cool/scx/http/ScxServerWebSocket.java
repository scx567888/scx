package cool.scx.http;

import cool.scx.http.uri.URIPath;
import cool.scx.http.uri.URIQuery;

import java.util.function.Consumer;

public interface ScxServerWebSocket {

    URIPath path();

    URIQuery query();

    ScxHttpHeaders headers();

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
