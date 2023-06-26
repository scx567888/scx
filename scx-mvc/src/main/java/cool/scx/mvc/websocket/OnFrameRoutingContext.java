package cool.scx.mvc.websocket;

import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

import java.lang.System.Logger;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.Logger.Level.ERROR;

/**
 * <p>OnFrameRoutingContext class.</p>
 *
 * @author scx567888
 * @version 1.18.1
 */
public class OnFrameRoutingContext {

    /**
     * Constant <code>logger</code>
     */
    private static final Logger logger = System.getLogger(OnFrameRoutingContext.class.getName());

    private final WebSocketFrame socketFrame;
    private final ServerWebSocket socket;
    private final Iterator<WebSocketRoute> iter;

    /**
     * <p>Constructor for OnFrameRoutingContext.</p>
     *
     * @param socketFrame a {@link io.vertx.core.http.WebSocketFrame} object
     * @param socket      a {@link io.vertx.core.http.ServerWebSocket} object
     * @param routes      a {@link java.util.List} object
     */
    OnFrameRoutingContext(WebSocketFrame socketFrame, ServerWebSocket socket, List<WebSocketRoute> routes) {
        this.socketFrame = socketFrame;
        this.socket = socket;
        this.iter = routes.iterator();
    }

    /**
     * <p>next.</p>
     */
    public void next() {
        while (iter.hasNext()) {
            var next = iter.next();
            if (next.matches(socket)) {
                if (socketFrame.isText()) {
                    try {
                        next.baseWebSocketHandler().onTextMessage(socketFrame.textData(), socketFrame, this.socket, this);
                    } catch (Exception e) {
                        logger.log(ERROR, "ScxWebSocketRoute : onTextMessage 发生异常 !!!", e);
                    }
                } else if (socketFrame.isBinary()) {
                    try {
                        next.baseWebSocketHandler().onBinaryMessage(socketFrame.binaryData(), socketFrame, this.socket, this);
                    } catch (Exception e) {
                        logger.log(ERROR, "ScxWebSocketRoute : onBinaryMessage 发生异常 !!!", e);
                    }
                }
                return;
            }
        }
    }

}
