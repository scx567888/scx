package cool.scx.web.websocket;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

import java.lang.System.Logger;
import java.util.List;

import static java.lang.System.Logger.Level.ERROR;

/**
 * <p>OnFrameRoutingContext class.</p>
 *
 * @author scx567888
 * @version 1.18.1
 */
public class OnFrameRoutingContext extends WebSocketRoutingContext {

    private static final Logger logger = System.getLogger(OnFrameRoutingContext.class.getName());
    private final WebSocketFrame frame;

    /**
     * <p>Constructor for OnFrameRoutingContext.</p>
     *
     * @param frame     a {@link io.vertx.core.http.WebSocketFrame} object
     * @param webSocket a {@link io.vertx.core.http.ServerWebSocket} object
     * @param routes    a {@link java.util.List} object
     */
    OnFrameRoutingContext(WebSocketFrame frame, ServerWebSocket webSocket, List<WebSocketRoute> routes) {
        super(webSocket, routes);
        this.frame = frame;
    }

    public WebSocketFrame frame() {
        return frame;
    }

    public String textData() {
        return frame.textData();
    }

    public Buffer binaryData() {
        return frame.binaryData();
    }

    @Override
    public void handle(WebSocketRoute next) {
        if (frame.isText()) {
            try {
                next.baseWebSocketHandler().onTextMessage(this);
            } catch (Exception e) {
                logger.log(ERROR, "ScxWebSocketRoute : onTextMessage 发生异常 !!!", e);
            }
        } else if (frame.isBinary()) {
            try {
                next.baseWebSocketHandler().onBinaryMessage(this);
            } catch (Exception e) {
                logger.log(ERROR, "ScxWebSocketRoute : onBinaryMessage 发生异常 !!!", e);
            }
        }
    }

}
