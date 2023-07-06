package cool.scx.mvc.websocket;

import io.vertx.core.http.ServerWebSocket;

import java.util.Iterator;
import java.util.List;

import static java.lang.System.Logger;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.getLogger;

/**
 * <p>OnExceptionRoutingContext class.</p>
 *
 * @author scx567888
 * @version 1.18.1
 */
public class OnExceptionRoutingContext {

    private static final Logger logger = getLogger(OnExceptionRoutingContext.class.getName());
    private final Throwable cause;
    private final ServerWebSocket webSocket;
    private final Iterator<WebSocketRoute> iter;

    OnExceptionRoutingContext(Throwable cause, ServerWebSocket webSocket, List<WebSocketRoute> scxWebSocketRoutes) {
        this.cause = cause;
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
                    next.baseWebSocketHandler().onError(this);
                } catch (Exception e) {
                    logger.log(ERROR, "ScxWebSocketRoute : onError 发生异常 !!!", e);
                }
                return;
            }
        }
    }

    public Throwable cause() {
        return cause;
    }

}
