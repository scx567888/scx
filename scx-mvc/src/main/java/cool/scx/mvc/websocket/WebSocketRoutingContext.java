package cool.scx.mvc.websocket;

import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;

import java.util.Iterator;
import java.util.List;

public abstract class WebSocketRoutingContext implements Handler<WebSocketRoute> {

    protected final ServerWebSocket webSocket;
    protected final Iterator<WebSocketRoute> iter;

    public WebSocketRoutingContext(ServerWebSocket webSocket, List<WebSocketRoute> webSocketRoutes) {
        this.webSocket = webSocket;
        this.iter = webSocketRoutes.iterator();
    }

    public final ServerWebSocket webSocket() {
        return webSocket;
    }

    public final void next() {
        while (iter.hasNext()) {
            var next = iter.next();
            if (next.matches(webSocket)) {
                handle(next);
                return;
            }
        }
    }

}
