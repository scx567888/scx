package cool.scx.web.routing;

import io.vertx.core.http.ServerWebSocket;

import java.lang.System.Logger;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Scx WebSocket 路由
 *
 * @author scx567888
 * @version 1.18.0
 */
public final class WebSocketRoute {

    /**
     * 日志
     */
    private static final Logger logger = System.getLogger(WebSocketRoute.class.getName());
    private final String path;
    private final Pattern pattern;
    private final int order;
    private final Consumer<WebSocketRoutingContext> handler;

    /**
     * <p>Constructor for ScxWebSocketRoute.</p>
     *
     * @param path                 a {@link java.lang.String} object
     * @param baseWebSocketHandler a {@link BaseWebSocketHandler} object
     */
    public WebSocketRoute(String path, Consumer<WebSocketRoutingContext> baseWebSocketHandler) {
        this(0, path, baseWebSocketHandler);
    }

    /**
     * <p>Constructor for ScxWebSocketRoute.</p>
     *
     * @param order                a int
     * @param path                 a {@link java.lang.String} object
     * @param baseWebSocketHandler a {@link BaseWebSocketHandler} object
     */
    public WebSocketRoute(int order, String path, Consumer<WebSocketRoutingContext> baseWebSocketHandler) {
        this.order = order;
        this.path = path;
        this.pattern = Pattern.compile(path);
        this.handler = baseWebSocketHandler;
    }

    /**
     * a
     *
     * @param serverWebSocket a
     * @return a
     */
    public boolean matches(ServerWebSocket serverWebSocket) {
        return this.pattern.matcher(serverWebSocket.path()).matches();
    }

    /**
     * <p>path.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String path() {
        return path;
    }

    /**
     * <p>baseWebSocketHandler.</p>
     *
     * @return a {@link BaseWebSocketHandler} object
     */
    public Consumer<WebSocketRoutingContext> handler() {
        return handler;
    }

    /**
     * <p>order.</p>
     *
     * @return a int
     */
    public int order() {
        return order;
    }

}
