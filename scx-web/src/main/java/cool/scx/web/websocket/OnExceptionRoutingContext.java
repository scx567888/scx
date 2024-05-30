package cool.scx.web.websocket;

import io.vertx.core.http.ServerWebSocket;

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
public class OnExceptionRoutingContext extends WebSocketRoutingContext {

    private static final Logger logger = getLogger(OnExceptionRoutingContext.class.getName());
    private final Throwable cause;

    OnExceptionRoutingContext(Throwable cause, ServerWebSocket webSocket, List<WebSocketRoute> scxWebSocketRoutes) {
        super(webSocket, scxWebSocketRoutes);
        this.cause = cause;
    }

    @Override
    public void handle(WebSocketRoute next) {
        try {
            next.baseWebSocketHandler().onError(this);
        } catch (Exception e) {
            logger.log(ERROR, "ScxWebSocketRoute : onError 发生异常 !!!", e);
        }
    }

    public Throwable cause() {
        return cause;
    }

}
