package cool.scx.http.helidon.test;

import cool.scx.http.helidon.HelidonHttpClient;
import cool.scx.http.helidon.HelidonHttpServer;
import cool.scx.http.helidon.HelidonHttpServerOptions;

public class WebSocketTest {

    public static void main(String[] args) {
        startServer();
        startClient();
    }

    public static void startServer() {
        var httpServer = new HelidonHttpServer(new HelidonHttpServerOptions().port(8080));

        httpServer.webSocketHandler(webSocket -> {
            webSocket.onTextMessage(data -> {
                webSocket.send(data);
                System.out.println("服 : " + data);
            });
        });

        httpServer.start();
    }

    public static void startClient() {
        var httpClient = new HelidonHttpClient();

        httpClient.webSocket().uri("ws://127.0.0.1:8080/websocket").onConnect(webSocket -> {
            webSocket.onTextMessage(data -> {
                System.out.println("客 : " + data);
            });
            //这里只有当 onConnect 走完才会 执行 来自客户端请求的监听 所以这里 创建线程发送 不阻塞 onConnect
            Thread.ofVirtual().start(() -> {
                for (int i = 0; i < 99999; i = i + 1) {
                    webSocket.send(i + "😀😀😀😀😀😀".repeat(100));
                }
            });
        }).connect();
    }

}
