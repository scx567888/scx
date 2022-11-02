package cool.scx.core.websocket;

import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class OnExceptionRoutingContext {

    private static final Logger logger = LoggerFactory.getLogger(OnExceptionRoutingContext.class);

    private final Throwable throwable;
    private final ServerWebSocket socket;
    private final Iterator<ScxWebSocketRoute> iter;

    public OnExceptionRoutingContext(Throwable e, ServerWebSocket serverWebSocket, List<ScxWebSocketRoute> scxWebSocketRoutes) {
        this.throwable = e;
        this.socket = serverWebSocket;
        this.iter = scxWebSocketRoutes.iterator();
    }

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
