package cool.scx.socket2;

import cool.scx.http.exception.UnauthorizedException;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;

import java.util.function.Consumer;

public class ScxSocketServer {

    private Consumer<ScxServerSocket> connectHandler;

    //不要阻塞这个回调 这个回调只用于设置 ScxSocket 的回调
    public void onConnect(Consumer<ScxServerSocket> onConnect) {
        this.connectHandler = onConnect;
    }

    public void call(ScxServerWebSocketHandshakeRequest request) {
        var clientID = request.getQuery("clientID");
        //没有 clientID 直接拒绝连接
        if (clientID == null) {
            throw new UnauthorizedException("Client ID required");
        }
        request.onWebSocket(webSocket -> {
            var serverSocket = new ScxServerSocket(webSocket,clientID);
            _callOnConnect(serverSocket);
        });
    }

    private void _callOnConnect(ScxServerSocket clientSocket) {
        if (this.connectHandler != null) {
            connectHandler.accept(clientSocket);
        }
    }

}
