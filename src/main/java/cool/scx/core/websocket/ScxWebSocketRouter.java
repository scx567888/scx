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
        if (anyMatch(serverWebSocket)) {
            new OnOpenRoutingContext(serverWebSocket, scxWebSocketRoutes).next();
            serverWebSocket
                    .frameHandler(h -> new OnFrameRoutingContext(h, serverWebSocket, scxWebSocketRoutes).next())
                    .exceptionHandler(e -> new OnExceptionRoutingContext(e, serverWebSocket, scxWebSocketRoutes).next())
                    .closeHandler(v -> new OnCloseRoutingContext(serverWebSocket, scxWebSocketRoutes).next());
        }
    }

    /**
     * <p>anyMatch.</p>
     *
     * @param serverWebSocket a {@link io.vertx.core.http.ServerWebSocket} object
     * @return a boolean
     */
    public boolean anyMatch(ServerWebSocket serverWebSocket) {
        boolean anyMatch = scxWebSocketRoutes.stream().anyMatch(c -> c.matches(serverWebSocket));
        if (!anyMatch) {
            //没有任何路由匹配 , 此处拒绝此 websocket 连接 使用 404 意味没有找到
            serverWebSocket.reject(404);
        }
        return anyMatch;
    }

}
