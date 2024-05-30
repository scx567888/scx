package cool.scx.web.websocket;

import io.vertx.core.http.ServerWebSocket;

import java.lang.System.Logger;
import java.util.List;

import static java.lang.System.Logger.Level.ERROR;

/**
 * <p>OnOpenRoutingContext class.</p>
 *
 * @author scx567888
 * @version 1.18.1
 */
public class OnOpenRoutingContext extends WebSocketRoutingContext {

    private static final Logger logger = System.getLogger(OnOpenRoutingContext.class.getName());

    OnOpenRoutingContext(ServerWebSocket webSocket, List<WebSocketRoute> routes) {
        super(webSocket, routes);
    }

    @Override
    public void handle(WebSocketRoute next) {
        try {
            next.baseWebSocketHandler().onOpen(this);
        } catch (Exception e) {
            logger.log(ERROR, "ScxWebSocketRoute : onOpen 发生异常 !!!", e);
        }
    }

}
