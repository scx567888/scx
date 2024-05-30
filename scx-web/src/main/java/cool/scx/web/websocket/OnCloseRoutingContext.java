package cool.scx.web.websocket;

import io.vertx.core.http.ServerWebSocket;

import java.lang.System.Logger;
import java.util.List;

import static java.lang.System.Logger.Level.ERROR;

/**
 * <p>OnCloseRoutingContext class.</p>
 *
 * @author scx567888
 * @version 1.18.1
 */
public class OnCloseRoutingContext extends WebSocketRoutingContext {

    private static final Logger logger = System.getLogger(OnCloseRoutingContext.class.getName());

    OnCloseRoutingContext(ServerWebSocket webSocket, List<WebSocketRoute> scxWebSocketRoutes) {
        super(webSocket, scxWebSocketRoutes);
    }

    @Override
    public void handle(WebSocketRoute next) {
        try {
            next.baseWebSocketHandler().onClose(this);
        } catch (Exception e) {
            logger.log(ERROR, "ScxWebSocketRoute : onClose 发生异常 !!!", e);
        }
    }

}
