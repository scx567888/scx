package cool.scx.core.websocket;

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
        boolean anyMatches = false;
        for (var route : scxWebSocketRoutes) {
            if (route.matches(serverWebSocket)) {
                route.handle(serverWebSocket);
                anyMatches = true;
            }
        }
        if (!anyMatches) {
            //没有任何路由匹配 , 此处拒绝此 websocket 连接 使用 404 意味没有找到
            serverWebSocket.reject(404);
        }
    }

}
