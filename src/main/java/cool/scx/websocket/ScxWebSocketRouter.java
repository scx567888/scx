package cool.scx.websocket;

import cool.scx.ScxBeanFactory;
import cool.scx.ScxModule;
import cool.scx.ScxModuleInfo;
import cool.scx.annotation.ScxWebSocketMapping;
import cool.scx.functional.ScxHandlerV;
import cool.scx.util.StringUtils;
import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a
 */
public final class ScxWebSocketRouter implements Handler<ServerWebSocket> {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ScxWebSocketRouter.class);

    /**
     * a
     */
    private final Map<String, ScxWebSocketRoute> scxWebSocketRouteMapping = new HashMap<>();

    public ScxWebSocketRouter(List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos, ScxBeanFactory scxBeanFactory) {
        scxModuleInfos.stream().flatMap(c -> c.scxWebSocketRouteClassList().stream()).forEach(c -> this.addRoute(new ScxWebSocketRoute(StringUtils.cleanHttpURL(c.getAnnotation(ScxWebSocketMapping.class).value()), scxBeanFactory.getBean(c))));
    }

    private static void handleException(ScxHandlerV s) {
        try {
            s.handle();
        } catch (Throwable e) {
            logger.error("ScxWebSocketRouter 发生异常 !!!", e);
        }
    }

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
        handleException(() -> handler.onOpen(webSocket));
        webSocket.frameHandler(h -> handleException(() -> {
                    if (h.isText()) {
                        handler.onTextMessage(h.textData(), h, webSocket);
                    } else if (h.isBinary()) {
                        handler.onBinaryMessage(h.binaryData(), h, webSocket);
                    }
                }))
                .exceptionHandler(event -> handleException(() -> handler.onError(event, webSocket)))
                .closeHandler(h -> handleException(() -> handler.onClose(webSocket)));
    }

}
