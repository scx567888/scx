package cool.scx.websocket;

import cool.scx.ScxHandler;
import cool.scx.ScxHandlerV;
import cool.scx.base.BaseWebSocketHandler;
import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Scx WebSocket 路由
 */
public record ScxWebSocketRoute(String path,
                                BaseWebSocketHandler baseWebSocketHandler) implements ScxHandler<ServerWebSocket> {

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

    public boolean matches(ServerWebSocket serverWebSocket) {
        return this.path.equals(serverWebSocket.path());
    }

    class FrameHandler implements Handler<WebSocketFrame> {

        private final ServerWebSocket serverWebSocket;

        public FrameHandler(ServerWebSocket serverWebSocket) {
            this.serverWebSocket = serverWebSocket;
        }

        @Override
        public void handle(WebSocketFrame h) {
            try {
                if (h.isText()) {
                    baseWebSocketHandler.onTextMessage(h.textData(), h, serverWebSocket);
                } else if (h.isBinary()) {
                    baseWebSocketHandler.onBinaryMessage(h.binaryData(), h, serverWebSocket);
                }
            } catch (Exception e) {
                logger.error("ScxWebSocketRouter 发生异常 !!!", e);
            }
        }
    }

    class ExceptionHandler implements Handler<Throwable> {

        public final ServerWebSocket serverWebSocket;

        public ExceptionHandler(ServerWebSocket serverWebSocket) {
            this.serverWebSocket = serverWebSocket;
        }

        @Override
        public void handle(Throwable event) {
            try {
                baseWebSocketHandler.onError(event, serverWebSocket);
            } catch (Exception e) {
                logger.error("ScxWebSocketRouter 发生异常 !!!", e);
            }
        }
    }

    class CloseHandler implements Handler<Void> {

        public final ServerWebSocket serverWebSocket;

        public CloseHandler(ServerWebSocket serverWebSocket) {
            this.serverWebSocket = serverWebSocket;
        }

        @Override
        public void handle(Void event) {
            try {
                baseWebSocketHandler.onClose(serverWebSocket);
            } catch (Exception e) {
                logger.error("ScxWebSocketRouter 发生异常 !!!", e);
            }
        }

    }

    class OpenHandler implements ScxHandlerV {

        public final ServerWebSocket serverWebSocket;

        public OpenHandler(ServerWebSocket serverWebSocket) {
            this.serverWebSocket = serverWebSocket;
        }

        @Override
        public void handle() {
            try {
                baseWebSocketHandler.onOpen(serverWebSocket);
            } catch (Exception e) {
                logger.error("ScxWebSocketRouter 发生异常 !!!", e);
            }
        }

    }

}