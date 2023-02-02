package cool.scx.mvc.base;

import cool.scx.mvc.annotation.ScxWebSocketMapping;
import cool.scx.mvc.websocket.OnCloseRoutingContext;
import cool.scx.mvc.websocket.OnExceptionRoutingContext;
import cool.scx.mvc.websocket.OnFrameRoutingContext;
import cool.scx.mvc.websocket.OnOpenRoutingContext;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

/**
 * BaseWebSocketHandler 基本接口 请与 {@link ScxWebSocketMapping} 配合使用
 *
 * @author scx567888
 * @version 1.0.10
 */
public interface BaseWebSocketHandler {

    /**
     * 连接 打开 时
     *
     * @param webSocket ServerWebSocket 连接对象
     * @param context   s
     * @throws java.lang.Exception e
     */
    default void onOpen(ServerWebSocket webSocket, OnOpenRoutingContext context) throws Exception {
        context.next();
    }

    /**
     * 发送 文本数据 时
     *
     * @param textData  文本数据
     * @param frame     帧对象
     * @param webSocket ServerWebSocket 连接对象
     * @param context   a
     * @throws java.lang.Exception e
     */
    default void onTextMessage(String textData, WebSocketFrame frame, ServerWebSocket webSocket, OnFrameRoutingContext context) throws Exception {
        context.next();
    }

    /**
     * 发送 二进制数据 时
     *
     * @param binaryData 二进制数据
     * @param frame      帧对象
     * @param webSocket  ServerWebSocket 连接对象
     * @param context    a
     * @throws java.lang.Exception e
     */
    default void onBinaryMessage(Buffer binaryData, WebSocketFrame frame, ServerWebSocket webSocket, OnFrameRoutingContext context) throws Exception {
        context.next();
    }

    /**
     * 连接 关闭 时
     *
     * @param webSocket ServerWebSocket 连接对象
     * @param context   a
     * @throws java.lang.Exception e
     */
    default void onClose(ServerWebSocket webSocket, OnCloseRoutingContext context) throws Exception {
        context.next();
    }

    /**
     * 连接 错误 时
     *
     * @param event     发生的错误
     * @param webSocket ServerWebSocket 连接对象
     * @param context   s
     * @throws java.lang.Exception e
     */
    default void onError(Throwable event, ServerWebSocket webSocket, OnExceptionRoutingContext context) throws Exception {
        context.next();
    }

}
