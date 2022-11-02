package cool.scx.core.websocket;

import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class OnCloseRoutingContext {

    private static final Logger logger = LoggerFactory.getLogger(OnCloseRoutingContext.class);

    private final ServerWebSocket socket;
    private final Iterator<ScxWebSocketRoute> iter;

    public OnCloseRoutingContext(ServerWebSocket serverWebSocket, List<ScxWebSocketRoute> scxWebSocketRoutes) {
        this.socket = serverWebSocket;
        this.iter = scxWebSocketRoutes.iterator();
    }

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
