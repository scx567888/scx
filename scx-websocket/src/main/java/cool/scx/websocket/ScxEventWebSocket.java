package cool.scx.websocket;

import cool.scx.websocket.handler.BinaryMessageHandler;
import cool.scx.websocket.handler.CloseHandler;
import cool.scx.websocket.handler.TextMessageHandler;

import java.util.function.Consumer;

/// 事件形式的 websocket
public interface ScxEventWebSocket extends ScxWebSocket {

    ScxEventWebSocket onTextMessage(TextMessageHandler textMessageHandler);

    ScxEventWebSocket onBinaryMessage(BinaryMessageHandler binaryMessageHandler);

    ScxEventWebSocket onPing(Consumer<byte[]> pingHandler);

    ScxEventWebSocket onPong(Consumer<byte[]> pongHandler);

    ScxEventWebSocket onClose(CloseHandler closeHandler);

    ScxEventWebSocket onError(Consumer<Throwable> errorHandler);

    /// 以上回调设置完成之后调用以便启动 websocket 监听
    void start();

}
