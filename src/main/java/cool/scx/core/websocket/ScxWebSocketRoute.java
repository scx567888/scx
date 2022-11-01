package cool.scx.core.websocket;

import cool.scx.core.base.BaseWebSocketHandler;
import cool.scx.functional.ScxHandler;
import cool.scx.functional.ScxHandlerA;
import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Scx WebSocket 路由
 *
 * @author scx567888
 * @version 1.17.11
 */
public final class ScxWebSocketRoute implements ScxHandlerA<ServerWebSocket> {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ScxWebSocketRoute.class);
    private final String path;
    private final Pattern pattern;
    private final BaseWebSocketHandler baseWebSocketHandler;
    private final int order;

    /**
     * <p>Constructor for ScxWebSocketRoute.</p>
     *
     * @param path                 a {@link java.lang.String} object
     * @param baseWebSocketHandler a {@link cool.scx.core.base.BaseWebSocketHandler} object
     */
    public ScxWebSocketRoute(String path, BaseWebSocketHandler baseWebSocketHandler) {
        this(0, path, baseWebSocketHandler);
    }

    /**
     * <p>Constructor for ScxWebSocketRoute.</p>
     *
     * @param order                a int
     * @param path                 a {@link java.lang.String} object
     * @param baseWebSocketHandler a {@link cool.scx.core.base.BaseWebSocketHandler} object
     */
    public ScxWebSocketRoute(int order, String path, BaseWebSocketHandler baseWebSocketHandler) {
        this.order = order;
        this.path = path;
        this.pattern = Pattern.compile(path);
        this.baseWebSocketHandler = baseWebSocketHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(ServerWebSocket serverWebSocket) {
        var openHandler = new OpenHandler(serverWebSocket);
        var frameHandler = new FrameHandler(serverWebSocket);
        var exceptionHandler = new ExceptionHandler(serverWebSocket);
        var closeHandler = new CloseHandler(serverWebSocket);
        openHandler.handle();
        serverWebSocket.frameHandler(frameHandler).exceptionHandler(exceptionHandler).closeHandler(closeHandler);
    }

    /**
     * a
     *
     * @param serverWebSocket a
     * @return a
     */
    public boolean matches(ServerWebSocket serverWebSocket) {
        return this.pattern.matcher(serverWebSocket.path()).matches();
    }

    /**
     * <p>path.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String path() {
        return path;
    }

    /**
     * <p>baseWebSocketHandler.</p>
     *
     * @return a {@link cool.scx.core.base.BaseWebSocketHandler} object
     */
    public BaseWebSocketHandler baseWebSocketHandler() {
        return baseWebSocketHandler;
    }

    /**
     * <p>order.</p>
     *
     * @return a int
     */
    public int order() {
        return order;
    }

    private class FrameHandler implements Handler<WebSocketFrame> {

        final ServerWebSocket serverWebSocket;

        FrameHandler(ServerWebSocket serverWebSocket) {
            this.serverWebSocket = serverWebSocket;
        }

        @Override
        public void handle(WebSocketFrame h) {
            if (h.isText()) {
                try {
                    baseWebSocketHandler.onTextMessage(h.textData(), h, serverWebSocket);
                } catch (Exception e) {
                    logger.error("ScxWebSocketRoute : onTextMessage 发生异常 !!!", e);
                }
            } else if (h.isBinary()) {
                try {
                    baseWebSocketHandler.onBinaryMessage(h.binaryData(), h, serverWebSocket);
                } catch (Exception e) {
                    logger.error("ScxWebSocketRoute : onBinaryMessage 发生异常 !!!", e);
                }
            }
        }
    }

    private class ExceptionHandler implements Handler<Throwable> {

        final ServerWebSocket serverWebSocket;

        ExceptionHandler(ServerWebSocket serverWebSocket) {
            this.serverWebSocket = serverWebSocket;
        }

        @Override
        public void handle(Throwable event) {
            try {
                baseWebSocketHandler.onError(event, serverWebSocket);
            } catch (Exception e) {
                logger.error("ScxWebSocketRoute : onError 发生异常 !!!", e);
            }
        }
    }

    private class CloseHandler implements Handler<Void> {

        final ServerWebSocket serverWebSocket;

        CloseHandler(ServerWebSocket serverWebSocket) {
            this.serverWebSocket = serverWebSocket;
        }

        @Override
        public void handle(Void event) {
            try {
                baseWebSocketHandler.onClose(serverWebSocket);
            } catch (Exception e) {
                logger.error("ScxWebSocketRoute : onClose 发生异常 !!!", e);
            }
        }

    }

    private class OpenHandler implements ScxHandler {

        final ServerWebSocket serverWebSocket;

        OpenHandler(ServerWebSocket serverWebSocket) {
            this.serverWebSocket = serverWebSocket;
        }

        @Override
        public void handle() {
            try {
                baseWebSocketHandler.onOpen(serverWebSocket);
            } catch (Exception e) {
                logger.error("ScxWebSocketRoute : onOpen 发生异常 !!!", e);
            }
        }

    }

}
