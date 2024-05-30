package cool.scx.web.websocket;

import cool.scx.common.util.MultiMap;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;

import java.util.Iterator;
import java.util.List;

public abstract class WebSocketRoutingContext implements Handler<WebSocketRoute> {

    protected final ServerWebSocket webSocket;
    protected final Iterator<WebSocketRoute> iter;
    private MultiMap<String, String> queryParams;

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

    public MultiMap<String, String> queryParams() {
        if (this.queryParams == null) {
            this.queryParams = new MultiMap<>();
            new QueryStringDecoder(this.webSocket.uri())
                    .parameters()
                    .forEach((key, value) -> this.queryParams.putAll(key, value));
        }
        return this.queryParams;
    }

    public List<String> queryParam(String query) {
        return this.queryParams().get(query);
    }

}
