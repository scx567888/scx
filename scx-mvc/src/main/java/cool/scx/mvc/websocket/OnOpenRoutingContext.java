package cool.scx.mvc.websocket;

import io.vertx.core.http.ServerWebSocket;

import java.lang.System.Logger;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.Logger.Level.ERROR;

/**
 * <p>OnOpenRoutingContext class.</p>
 *
 * @author scx567888
 * @version 1.18.1
 */
public class OnOpenRoutingContext {

    /**
     * Constant <code>logger</code>
     */
    private static final Logger logger = System.getLogger(OnOpenRoutingContext.class.getName());

    private final ServerWebSocket socket;
    private final Iterator<WebSocketRoute> iter;

    /**
     * <p>Constructor for OnOpenRoutingContext.</p>
     *
     * @param socket a {@link io.vertx.core.http.ServerWebSocket} object
     * @param routes a {@link java.util.List} object
     */
    OnOpenRoutingContext(ServerWebSocket socket, List<WebSocketRoute> routes) {
        this.socket = socket;
        this.iter = routes.iterator();
    }

    /**
     * <p>next.</p>
     */
    public void next() {
        while (iter.hasNext()) {
            var next = iter.next();
            if (next.matches(socket)) {
                try {
                    next.baseWebSocketHandler().onOpen(socket, this);
                } catch (Exception e) {
                    logger.log(ERROR, "ScxWebSocketRoute : onOpen 发生异常 !!!", e);
                }
                return;
            }
        }
    }

}
