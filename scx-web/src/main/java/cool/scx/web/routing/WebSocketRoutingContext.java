package cool.scx.web.routing;

import cool.scx.common.util.MultiMap;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.vertx.core.http.ServerWebSocket;

import java.util.Iterator;
import java.util.List;

import static java.lang.System.Logger.Level.ERROR;

public class WebSocketRoutingContext {

    private static final System.Logger logger = System.getLogger(WebSocketRoutingContext.class.getName());

    private final ServerWebSocket webSocket;
    private final Iterator<WebSocketRoute> iter;
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

    public void handle(WebSocketRoute route) {
        try {
            route.handler().accept(this);
        } catch (Exception e) {
            logger.log(ERROR, "ScxWebSocketRoute : onOpen 发生异常 !!!", e);
        }
    }

}
