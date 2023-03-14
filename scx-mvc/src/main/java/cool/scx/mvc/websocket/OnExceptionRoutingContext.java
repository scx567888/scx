package cool.scx.mvc.websocket;

import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * <p>OnExceptionRoutingContext class.</p>
 *
 * @author scx567888
 * @version 1.18.1
 */
public class OnExceptionRoutingContext {

    /**
     * Constant <code>logger</code>
     */
    private static final Logger logger = LoggerFactory.getLogger(OnExceptionRoutingContext.class);

    private final Throwable throwable;
    private final ServerWebSocket socket;
    private final Iterator<WebSocketRoute> iter;

    /**
     * <p>Constructor for OnExceptionRoutingContext.</p>
     *
     * @param e                  a {@link java.lang.Throwable} object
     * @param serverWebSocket    a {@link io.vertx.core.http.ServerWebSocket} object
     * @param scxWebSocketRoutes a {@link java.util.List} object
     */
    OnExceptionRoutingContext(Throwable e, ServerWebSocket serverWebSocket, List<WebSocketRoute> scxWebSocketRoutes) {
        this.throwable = e;
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
                    next.baseWebSocketHandler().onError(throwable, this.socket, this);
                } catch (Exception e) {
                    logger.error("ScxWebSocketRoute : onError 发生异常 !!!", e);
                }
                return;
            }
        }
    }

}
