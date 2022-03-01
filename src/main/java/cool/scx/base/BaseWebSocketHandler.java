package cool.scx.base;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

/**
 * BaseWSHandler 基本接口 请与 ScxWebSocketRoute 配合使用
 *
 * @author scx567888
 * @version 1.0.10
 */
public interface BaseWebSocketHandler {

    /**
     * 连接打开时
     *
     * @param webSocket a {@link io.vertx.core.http.ServerWebSocket} object.
     * @throws java.lang.Exception e
     */
    void onOpen(ServerWebSocket webSocket) throws Exception;

    /**
     * 连接关闭时
     *
     * @param webSocket a {@link io.vertx.core.http.ServerWebSocket} object.
     * @throws java.lang.Exception e
     */
    void onClose(ServerWebSocket webSocket) throws Exception;

    /**
     * 发送 文本数据
     *
     * @param textData  a {@link java.lang.String} object.
     * @param h         a {@link io.vertx.core.http.WebSocketFrame} object.
     * @param webSocket a {@link io.vertx.core.http.ServerWebSocket} object.
     * @throws java.lang.Exception e
     */
    void onTextMessage(String textData, WebSocketFrame h, ServerWebSocket webSocket) throws Exception;

    /**
     * 发送二进制数据
     *
     * @param binaryData a {@link io.vertx.core.buffer.Buffer} object.
     * @param h          a {@link io.vertx.core.http.WebSocketFrame} object.
     * @param webSocket  a {@link io.vertx.core.http.ServerWebSocket} object.
     * @throws java.lang.Exception e
     */
    void onBinaryMessage(Buffer binaryData, WebSocketFrame h, ServerWebSocket webSocket) throws Exception;

    /**
     * 连接错误时
     *
     * @param event     a {@link java.lang.Throwable} object.
     * @param webSocket a {@link io.vertx.core.http.ServerWebSocket} object.
     * @throws java.lang.Exception e
     */
    void onError(Throwable event, ServerWebSocket webSocket) throws Exception;

}
