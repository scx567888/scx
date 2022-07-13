package cool.scx.core.websocket;

import cool.scx.core.base.BaseWebSocketHandler;
import cool.scx.functional.ScxHandler;
import cool.scx.functional.ScxHandlerA;
import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Scx WebSocket 路由
 */
public record ScxWebSocketRoute(String path,
                                BaseWebSocketHandler baseWebSocketHandler) implements ScxHandlerA<ServerWebSocket> {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ScxWebSocketRoute.class);

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
        return this.path.equals(serverWebSocket.path());
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