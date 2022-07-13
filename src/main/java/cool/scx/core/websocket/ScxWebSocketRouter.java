package cool.scx.core.websocket;

import cool.scx.core.ScxBeanFactory;
import cool.scx.core.ScxModuleMetadata;
import cool.scx.core.annotation.ScxWebSocketMapping;
import cool.scx.util.URIBuilder;
import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxWebSocketRouter implements Handler<ServerWebSocket> {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ScxWebSocketRouter.class);

    /**
     * a
     */
    private final List<ScxWebSocketRoute> scxWebSocketRoutes = new ArrayList<>();

    /**
     * a
     *
     * @param metadataList   a
     * @param scxBeanFactory a
     */
    public ScxWebSocketRouter(List<ScxModuleMetadata<?>> metadataList, ScxBeanFactory scxBeanFactory) {
        for (var m : metadataList) {
            for (var clazz : m.scxWebSocketRouteClassList()) {
                var path = URIBuilder.join(clazz.getAnnotation(ScxWebSocketMapping.class).value());
                var baseWebSocketHandler = scxBeanFactory.getBean(clazz);
                addRoute(new ScxWebSocketRoute(path, baseWebSocketHandler));
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
        scxWebSocketRoutes.add(scxRoute);
        return this;
    }

    /**
     * a
     *
     * @return a
     */
    public List<ScxWebSocketRoute> getRoutes() {
        return new ArrayList<>(scxWebSocketRoutes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(ServerWebSocket serverWebSocket) {
        for (var route : scxWebSocketRoutes) {
            if (route.matches(serverWebSocket)) {
                route.handle(serverWebSocket);
                return;
            }
        }
        //没有任何路由匹配 , 此处拒绝此 websocket 连接 使用 404 意味没有找到
        serverWebSocket.reject(404);
    }

}
