package cool.scx.socket;

import cool.scx.http.web_socket.ScxWebSocket;
import cool.scx.http.web_socket.ScxWebSocketCloseInfo;

/**
 * 客户端 Socket 对象


 * @author scx567888
 * @version 0.0.1
 */
public final class ScxClientSocket extends PingPongManager {

    private final ScxSocketClient socketClient;

    ScxClientSocket(ScxWebSocket webSocket, String clientID, ScxSocketClient socketClient) {
        super(webSocket, clientID, socketClient.options);
        this.socketClient = socketClient;
    }

    ScxClientSocket(ScxWebSocket webSocket, String clientID, ScxSocketClient socketClient, ScxSocketStatus status) {
        super(webSocket, clientID, socketClient.options, status);
        this.socketClient = socketClient;
    }

    @Override
    protected void doClose(ScxWebSocketCloseInfo closeInfo) {
        super.doClose(closeInfo);
        this.socketClient.connect();
    }

    @Override
    protected void doError(Throwable e) {
        super.doError(e);
        this.socketClient.connect();
    }

    @Override
    public void close() {
        this.socketClient.removeConnectFuture();
        this.socketClient.cancelReconnect();
        this.resetCloseOrErrorBind();
        super.close();
    }

    /**
     * 重置 关闭和 错误的 handler
     */
    private void resetCloseOrErrorBind() {
        if (!this.webSocket.isClosed()) {
            this.webSocket.onClose(null);
            this.webSocket.onError(null);
        }
    }

    @Override
    protected void doPingTimeout() {
        //心跳失败直接重连
        this.socketClient.connect();
    }

}
