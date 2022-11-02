package cool.scx.core.websocket;

import cool.scx.core.base.BaseWebSocketHandler;
import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Scx WebSocket 路由
 *
 * @author scx567888
 * @version 1.18.0
 */
public final class ScxWebSocketRoute {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ScxWebSocketRoute.class);
    private final String path;
    private final Pattern pattern;
    private final BaseWebSocketHandler baseWebSocketHandler;
    private final int order;

    /**
     * <p>Constructor for ScxWebSocketRoute.</p>
     *
     * @param path                 a {@link java.lang.String} object
     * @param baseWebSocketHandler a {@link cool.scx.core.base.BaseWebSocketHandler} object
     */
    public ScxWebSocketRoute(String path, BaseWebSocketHandler baseWebSocketHandler) {
        this(0, path, baseWebSocketHandler);
    }

    /**
     * <p>Constructor for ScxWebSocketRoute.</p>
     *
     * @param order                a int
     * @param path                 a {@link java.lang.String} object
     * @param baseWebSocketHandler a {@link cool.scx.core.base.BaseWebSocketHandler} object
     */
    public ScxWebSocketRoute(int order, String path, BaseWebSocketHandler baseWebSocketHandler) {
        this.order = order;
        this.path = path;
        this.pattern = Pattern.compile(path);
        this.baseWebSocketHandler = baseWebSocketHandler;
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
     * @return a {@link cool.scx.core.base.BaseWebSocketHandler} object
     */
    public BaseWebSocketHandler baseWebSocketHandler() {
        return baseWebSocketHandler;
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
