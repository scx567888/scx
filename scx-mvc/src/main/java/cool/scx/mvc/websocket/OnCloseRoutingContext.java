package cool.scx.mvc.websocket;

import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * <p>OnCloseRoutingContext class.</p>
 *
 * @author scx567888
 * @version 1.18.1
 */
public class OnCloseRoutingContext {

    /**
     * Constant <code>logger</code>
     */
    private static final Logger logger = LoggerFactory.getLogger(OnCloseRoutingContext.class);

    private final ServerWebSocket socket;
    private final Iterator<WebSocketRoute> iter;

    /**
     * <p>Constructor for OnCloseRoutingContext.</p>
     *
     * @param serverWebSocket    a {@link io.vertx.core.http.ServerWebSocket} object
     * @param scxWebSocketRoutes a {@link java.util.List} object
     */
    OnCloseRoutingContext(ServerWebSocket serverWebSocket, List<WebSocketRoute> scxWebSocketRoutes) {
        this.socket = serverWebSocket;
        this.iter = scxWebSocketRoutes.iterator();
    }

    /**
     * <p>next.</p>
     */
    public void next() {
        while (iter.hasNext()) {
            var next = iter.next();
            if (next.matches(socket)) {
                try {
                    next.baseWebSocketHandler().onClose(this.socket, this);
                } catch (Exception e) {
                    logger.error("ScxWebSocketRoute : onClose 发生异常 !!!", e);
                }
                return;
            }
        }
    }

}
