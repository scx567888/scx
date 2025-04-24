package cool.scx.websocket;

import static cool.scx.websocket.WebSocketCloseInfo.NORMAL_CLOSE;
import static cool.scx.websocket.WebSocketHelper.createClosePayload;
import static cool.scx.websocket.WebSocketOpCode.*;

/// ScxWebSocket
///
/// @author scx567888
/// @version 0.0.1
public interface ScxWebSocket {

    WebSocketFrame readFrame() throws WebSocketException;

    ScxWebSocket sendFrame(WebSocketFrame frame);

    ScxWebSocket terminate();

    boolean isClosed();

    default ScxWebSocket send(String textMessage, boolean last) {
        var payload = textMessage != null ? textMessage.getBytes() : new byte[]{};
        var frame = WebSocketFrame.of(TEXT, payload, last);
        sendFrame(frame);
        return this;
    }

    default ScxWebSocket send(byte[] binaryMessage, boolean last) {
        var frame = WebSocketFrame.of(BINARY, binaryMessage, last);
        sendFrame(frame);
        return this;
    }

    default ScxWebSocket ping(byte[] data) {
        var frame = WebSocketFrame.of(PING, data);
        sendFrame(frame);
        return this;
    }

    default ScxWebSocket pong(byte[] data) {
        var frame = WebSocketFrame.of(PONG, data, false);
        sendFrame(frame);
        return this;
    }

    default ScxWebSocket close(int code, String reason) {
        var closePayload = createClosePayload(code, reason);
        var frame = WebSocketFrame.of(CLOSE, closePayload, true);
        sendFrame(frame);
        return this;
    }

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
