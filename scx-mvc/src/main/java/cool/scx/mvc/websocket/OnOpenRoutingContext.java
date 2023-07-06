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

    private static final Logger logger = System.getLogger(OnOpenRoutingContext.class.getName());
    private final ServerWebSocket webSocket;
    private final Iterator<WebSocketRoute> iter;

    OnOpenRoutingContext(ServerWebSocket webSocket, List<WebSocketRoute> routes) {
        this.webSocket = webSocket;
        this.iter = routes.iterator();
    }

    public ServerWebSocket webSocket() {
        return webSocket;
    }

    public void next() {
        while (iter.hasNext()) {
            var next = iter.next();
            if (next.matches(webSocket)) {
                try {
                    next.baseWebSocketHandler().onOpen(this);
                } catch (Exception e) {
                    logger.log(ERROR, "ScxWebSocketRoute : onOpen 发生异常 !!!", e);
                }
                return;
            }
        }
    }

}
