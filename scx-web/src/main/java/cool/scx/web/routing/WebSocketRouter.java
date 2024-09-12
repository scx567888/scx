package cool.scx.web.routing;

import cool.scx.http.ScxServerWebSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WebSocketRouter implements Consumer<ScxServerWebSocket> {

    private final List<WebSocketRoute> routes;

    public WebSocketRouter() {
        this.routes = new ArrayList<>();
    }

    public WebSocketRouter addRoute(WebSocketRoute route) {
        routes.add(route.order(), route);
        return this;
    }

    public List<WebSocketRoute> getRoutes() {
        return new ArrayList<>(routes);
    }

    @Override
    public void accept(ScxServerWebSocket scxServerWebSocket) {
        if (anyMatch(scxServerWebSocket)) {
            var routingContext = new WebSocketRoutingContext(scxServerWebSocket, this.routes);
            routingContext.next();
        }
    }

    /**
     * <p>anyMatch.</p>
     *
     * @param serverWebSocket a {@link io.vertx.core.http.ServerWebSocket} object
     * @return a boolean
     */
    private boolean anyMatch(ScxServerWebSocket serverWebSocket) {
        boolean anyMatch = routes.stream().anyMatch(c -> c.matches(serverWebSocket));
        if (!anyMatch) {
            //没有任何路由匹配 , 此处拒绝此 websocket 连接 使用 404 意味没有找到
            serverWebSocket.close(404, "");
        }
        return anyMatch;
    }

}
