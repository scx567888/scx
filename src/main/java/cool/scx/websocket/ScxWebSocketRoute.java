package cool.scx.websocket;

import cool.scx.base.BaseWebSocketHandler;

/**
 * Scx WebSocket 路由
 */
public record ScxWebSocketRoute(String path, BaseWebSocketHandler baseWebSocketHandler) {

}