package cool.scx.web.websocket;

import cool.scx.web.annotation.ScxWebSocketRoute;

/**
 * BaseWebSocketHandler 基本接口 请与 {@link ScxWebSocketRoute} 配合使用
 *
 * @author scx567888
 * @version 1.0.10
 */
public interface BaseWebSocketHandler {

    /**
     * 连接 打开 时
     *
     * @param context 上下文
     * @throws java.lang.Exception e
     */
    default void onOpen(OnOpenRoutingContext context) throws Exception {
        context.next();
    }

    /**
     * 发送 文本数据 时
     *
     * @param context 上下文
     * @throws java.lang.Exception e
     */
    default void onTextMessage(OnFrameRoutingContext context) throws Exception {
        context.next();
    }

    /**
     * 发送 二进制数据 时
     *
     * @param context 上下文
     * @throws java.lang.Exception e
     */
    default void onBinaryMessage(OnFrameRoutingContext context) throws Exception {
        context.next();
    }

    /**
     * 连接 关闭 时
     *
     * @param context 上下文
     * @throws java.lang.Exception e
     */
    default void onClose(OnCloseRoutingContext context) throws Exception {
        context.next();
    }

    /**
     * 连接 错误 时
     *
     * @param context 上下文
     * @throws java.lang.Exception e
     */
    default void onError(OnExceptionRoutingContext context) throws Exception {
        context.next();
    }

}
