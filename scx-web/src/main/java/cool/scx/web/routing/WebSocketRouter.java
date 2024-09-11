package cool.scx.web.routing;

import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class WebSocketRouter implements Handler<ServerWebSocket> {

    /**
     * 日志
     */
    private static final Logger logger = System.getLogger(WebSocketRouter.class.getName());

    /**
     * a
     */
    private final List<WebSocketRoute> routes = new ArrayList<>();

    /**
     * 添加一个路由
     *
     * @param scxRoute s
     * @return s
     */
    public WebSocketRouter addRoute(WebSocketRoute scxRoute) {
        routes.add(scxRoute);
        return this;
    }

    /**
     * a
     *
     * @return a
     */
    public List<WebSocketRoute> getRoutes() {
        return new ArrayList<>(routes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(ServerWebSocket serverWebSocket) {
        if (anyMatch(serverWebSocket)) {
            new WebSocketRoutingContext(serverWebSocket, routes).next();
        }
    }

    /**
     * <p>anyMatch.</p>
     *
     * @param serverWebSocket a {@link io.vertx.core.http.ServerWebSocket} object
     * @return a boolean
     */
    private boolean anyMatch(ServerWebSocket serverWebSocket) {
        boolean anyMatch = routes.stream().anyMatch(c -> c.matches(serverWebSocket));
        if (!anyMatch) {
            //没有任何路由匹配 , 此处拒绝此 websocket 连接 使用 404 意味没有找到
            serverWebSocket.reject(404);
        }
        return anyMatch;
    }

}
