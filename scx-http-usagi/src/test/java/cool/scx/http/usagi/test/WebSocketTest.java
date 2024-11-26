package cool.scx.http.usagi.test;

import cool.scx.http.usagi.UsagiHttpClient;
import cool.scx.http.usagi.UsagiHttpServer;
import cool.scx.http.usagi.UsagiHttpServerOptions;

public class WebSocketTest {

    public static void main(String[] args) {
        startServer();
        startClient();
    }

    public static void startServer() {
        var httpServer = new UsagiHttpServer(new UsagiHttpServerOptions().port(8080));

        httpServer.webSocketHandler(webSocket -> {
            webSocket.onTextMessage(data -> {
                webSocket.send(data);
                System.out.println("服 : " + data);
            });
        });

        httpServer.start();
    }

    public static void startClient() {
        var httpClient = new UsagiHttpClient();

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
