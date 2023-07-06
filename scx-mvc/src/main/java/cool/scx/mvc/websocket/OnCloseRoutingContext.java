package cool.scx.mvc.websocket;

import io.vertx.core.http.ServerWebSocket;

import java.lang.System.Logger;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.Logger.Level.ERROR;

/**
 * <p>OnCloseRoutingContext class.</p>
 *
 * @author scx567888
 * @version 1.18.1
 */
public class OnCloseRoutingContext {

    private static final Logger logger = System.getLogger(OnCloseRoutingContext.class.getName());
    private final ServerWebSocket webSocket;
    private final Iterator<WebSocketRoute> iter;

    OnCloseRoutingContext(ServerWebSocket webSocket, List<WebSocketRoute> scxWebSocketRoutes) {
        this.webSocket = webSocket;
        this.iter = scxWebSocketRoutes.iterator();
    }

    public ServerWebSocket webSocket() {
        return webSocket;
    }

    public void next() {
        while (iter.hasNext()) {
            var next = iter.next();
            if (next.matches(webSocket)) {
                try {
                    next.baseWebSocketHandler().onClose(this);
                } catch (Exception e) {
                    logger.log(ERROR, "ScxWebSocketRoute : onClose 发生异常 !!!", e);
                }
                return;
            }
        }
    }

}
