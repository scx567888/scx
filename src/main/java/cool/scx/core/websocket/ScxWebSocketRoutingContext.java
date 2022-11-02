package cool.scx.core.websocket;

import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class ScxWebSocketRoutingContext {

    private static final Logger logger = LoggerFactory.getLogger(ScxWebSocketRoutingContext.class);

    private final ServerWebSocket socket;
    private final List<ScxWebSocketRoute> routes;
    private final Iterator<ScxWebSocketRoute> iter;

    public ScxWebSocketRoutingContext(ServerWebSocket socket, List<ScxWebSocketRoute> routes) {
        this.socket = socket;
        this.routes = routes;
        this.iter = routes.iterator();
    }

    public void nextOnOpen() {
        while (iter.hasNext()) {
            var next = iter.next();
            if (next.matches(socket)) {
                try {
                    next.baseWebSocketHandler().onOpen(socket, this);
                } catch (Exception e) {
                    logger.error("ScxWebSocketRoute : onOpen 发生异常 !!!", e);
                }
                return;
            }
        }
    }

    public void nextOnFrame(WebSocketFrame h) {
        while (iter.hasNext()) {
            var next = iter.next();
            if (next.matches(socket)) {
                if (h.isText()) {
                    try {
                        next.baseWebSocketHandler().onTextMessage(h.textData(), h, this.socket, this);
                    } catch (Exception e) {
                        logger.error("ScxWebSocketRoute : onTextMessage 发生异常 !!!", e);
                    }
                } else if (h.isBinary()) {
                    try {
                        next.baseWebSocketHandler().onBinaryMessage(h.binaryData(), h, this.socket, this);
                    } catch (Exception e) {
                        logger.error("ScxWebSocketRoute : onBinaryMessage 发生异常 !!!", e);
                    }
                }
                return;
            }
        }
    }


    public void nextOnException(Throwable throwable) {
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

    public void nextOnClose(Void unused) {
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
