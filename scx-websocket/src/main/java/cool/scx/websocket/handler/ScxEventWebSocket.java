package cool.scx.websocket.handler;

import cool.scx.websocket.ScxWebSocket;

import java.util.function.Consumer;

/// todo 支持自定义 执行器
/// 事件形式的 websocket
public interface ScxEventWebSocket extends ScxWebSocket {

    static ScxEventWebSocket of(ScxWebSocket scxWebSocket) {
        return new ScxEventWebSocketImpl(scxWebSocket);
    }

    ScxEventWebSocket onTextMessage(TextMessageHandler textMessageHandler);

    ScxEventWebSocket onBinaryMessage(BinaryMessageHandler binaryMessageHandler);

    ScxEventWebSocket onPing(Consumer<byte[]> pingHandler);

    ScxEventWebSocket onPong(Consumer<byte[]> pongHandler);

    ScxEventWebSocket onClose(CloseHandler closeHandler);

    ScxEventWebSocket onError(Consumer<Throwable> errorHandler);

    /// 以上回调设置完成之后调用以便启动 websocket 监听
    void start();

}
