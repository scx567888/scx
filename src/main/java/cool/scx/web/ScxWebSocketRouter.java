package cool.scx.web;

import cool.scx.ScxBeanFactory;
import cool.scx.ScxModule;
import cool.scx.ScxModuleInfo;
import cool.scx.annotation.ScxWebSocketMapping;
import cool.scx.config.ScxEasyConfig;
import cool.scx.mvc.exception_handler.ScxMappingExceptionHandler;
import cool.scx.util.StringUtils;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
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

    public ScxWebSocketRouter(ScxEasyConfig scxEasyConfig, Vertx vertx, List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos, ScxBeanFactory scxBeanFactory) {
        registerAllRoute(scxModuleInfos, scxBeanFactory);
    }

    public void registerAllRoute(List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos, ScxBeanFactory scxBeanFactory) {
        for (var scxModuleInfo : scxModuleInfos) {
            for (var c : scxModuleInfo.scxWebSocketRouteClassList()) {
                var annotation = c.getAnnotation(ScxWebSocketMapping.class);
                if (annotation != null) {
                    this.addRoute(new ScxWebSocketRoute()
                            .path(StringUtils.cleanHttpURL(annotation.value()))
                            .baseWebSocketHandler(scxBeanFactory.getBean(c)));
                }
            }
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
        handler.onOpen(webSocket);
        //　WebSocket 连接
        webSocket.frameHandler(h -> {
            try {
                if (h.isText()) {
                    handler.onTextMessage(h.textData(), h, webSocket);
                } else if (h.isBinary()) {
                    handler.onBinaryMessage(h.binaryData(), h, webSocket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        webSocket.exceptionHandler(event -> handler.onError(event, webSocket));
        webSocket.closeHandler(h -> handler.onClose(webSocket));
    }

    public ScxWebSocketRouter init() {
        return this;
    }

}
