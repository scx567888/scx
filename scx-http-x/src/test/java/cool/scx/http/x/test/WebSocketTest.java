package cool.scx.http.x.test;

import cool.scx.common.util.$;
import cool.scx.http.web_socket.ScxServerWebSocket;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;
import cool.scx.http.x.XHttpClient;
import cool.scx.http.x.XHttpServer;
import cool.scx.http.x.XHttpServerOptions;

public class WebSocketTest {

    public static void main(String[] args) {
        startServer();
        startClient();
    }

    public static void startServer() {
        var s = System.nanoTime();
        var httpServer = new XHttpServer(new XHttpServerOptions().port(8080));

        httpServer.onRequest(request -> {
            if (request instanceof ScxServerWebSocketHandshakeRequest wsRequest) {
                var webSocket = wsRequest.response().acceptHandshake();
                webSocket.onTextMessage((data, _) -> {
                    webSocket.send(data);
                    System.out.println("服 : " + data);
                });    
                webSocket.startListening();
            }
        });

        httpServer.start();
        System.out.println("http server started " + (System.nanoTime() - s) / 1000_000);
    }

    public static void startClient() {
        var httpClient = new XHttpClient();

        var response = httpClient.webSocketHandshakeRequest().uri("ws://127.0.0.1:8080/websocket").send();
        var webSocket = response.webSocket();
        webSocket.onTextMessage((data, s) -> {
            System.out.println("客 : " + data);
        });
        
        //这里只有当 onConnect 走完才会 执行 来自客户端请求的监听 所以这里 创建线程发送 不阻塞 onConnect
        Thread.ofVirtual().start(() -> {
            for (int i = 0; i < 99999; i = i + 1) {
                webSocket.send(i + "😀😀😀😀😀😀".repeat(100));
            }
        });
        Thread.ofVirtual().start(() -> {
            webSocket.startListening();    
        });
        
    }

}
