package cool.scx.http.web_socket;

import cool.scx.http.web_socket.handler.BinaryMessageHandler;
import cool.scx.http.web_socket.handler.CloseHandler;
import cool.scx.http.web_socket.handler.TextMessageHandler;

import java.util.function.Consumer;

import static cool.scx.http.web_socket.WebSocketCloseInfo.NORMAL_CLOSE;

/**
 * ScxWebSocket
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxWebSocket {

    ScxWebSocket onTextMessage(TextMessageHandler textMessageHandler);

    ScxWebSocket onBinaryMessage(BinaryMessageHandler binaryMessageHandler);

    ScxWebSocket onPing(Consumer<byte[]> pingHandler);

    ScxWebSocket onPong(Consumer<byte[]> pongHandler);

    ScxWebSocket onClose(CloseHandler closeHandler);

    ScxWebSocket onError(Consumer<Throwable> errorHandler);

    //以上回调设置完成之后调用以便启动 websocket 监听
    ScxWebSocket startListening();

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

    default ScxWebSocket close(WebSocketCloseInfo closeInfo) {
        return close(closeInfo.code(), closeInfo.reason());
    }

    default ScxWebSocket close() {
        return close(NORMAL_CLOSE);
    }

}
