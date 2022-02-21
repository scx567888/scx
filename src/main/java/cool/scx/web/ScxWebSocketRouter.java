package cool.scx.web;

import cool.scx.mvc.exception_handler.ScxMappingExceptionHandler;
import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a
 */
public final class ScxWebSocketRouter implements Handler<ServerWebSocket> {

    /**
     * 异常处理器
     */
    private final List<ScxMappingExceptionHandler> scxMappingExceptionHandlers = new ArrayList<>();

    /**
     * a
     */
    private final Map<String, ScxWebSocketRoute> scxWebSocketRouteMapping = new HashMap<>();

    /**
     * 添加一个路由
     *
     * @param scxRoute s
     * @return s
     */
    public ScxWebSocketRouter addRoute(ScxWebSocketRoute scxRoute) {
        if (scxRoute.path() != null) {
            scxWebSocketRouteMapping.put(scxRoute.path(), scxRoute);
        }
        return this;
    }

    /**
     * a
     *
     * @return a
     */
    public List<ScxWebSocketRoute> getRoutes() {
        return new ArrayList<>(scxWebSocketRouteMapping.values());
    }

    @Override
    public void handle(ServerWebSocket webSocket) {
        var route = scxWebSocketRouteMapping.get(webSocket.path());
        if (route == null) {
            //此处拒绝此 websocket 连接 使用 404 意味没有找到
            webSocket.reject(404);
            return;
        }
        var handler = route.baseWebSocketHandler();
        handler.onOpen(webSocket);
        //　WebSocket 连接
        webSocket.frameHandler(h -> {
            if (h.isText()) {
                handler.onTextMessage(h.textData(), h, webSocket);
            } else if (h.isBinary()) {
                handler.onBinaryMessage(h.binaryData(), h, webSocket);
            }
        });
        webSocket.exceptionHandler(event -> handler.onError(event, webSocket));
        webSocket.closeHandler(h -> handler.onClose(webSocket));
    }

}
