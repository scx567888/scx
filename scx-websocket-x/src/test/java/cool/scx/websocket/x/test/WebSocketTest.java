package cool.scx.websocket.x.test;

import cool.scx.http.x.XHttpServer;
import cool.scx.http.x.XHttpServerOptions;
import cool.scx.websocket.ScxServerWebSocketHandshakeRequest;
import cool.scx.websocket.handler.ScxEventWebSocket;
import cool.scx.websocket.x.WebSocketUpgradeHandler;
import cool.scx.websocket.x.XWebSocketClient;

public class WebSocketTest {

    public static void main(String[] args) {
        startServer();
        startClient();
    }

    public static void startServer() {
        var s = System.nanoTime();
        var httpServer = new XHttpServer(new XHttpServerOptions().port(8080).addUpgradeHandlerList(new WebSocketUpgradeHandler()));

        httpServer.onRequest(req -> {
            if (req instanceof ScxServerWebSocketHandshakeRequest wsReq) {
                var webSocket = wsReq.webSocket();
                //可以以这种 偏底层的方式使用
                while (true) {
                    var frame = webSocket.readFrame();
                    var data = new String(frame.payloadData());
                    webSocket.send(data);
                    System.out.println("服 : " + data);
                }
            }
        });

        httpServer.start();
        System.out.println("http server started " + (System.nanoTime() - s) / 1000_000);
    }

    public static void startClient() {
        var httpClient = new XWebSocketClient();

        var webSocket = httpClient.webSocketHandshakeRequest().uri("ws://127.0.0.1:8080/websocket").webSocket();

        //这里只有当 onConnect 走完才会 执行 来自客户端请求的监听 所以这里 创建线程发送 不阻塞 onConnect
        Thread.ofVirtual().start(() -> {
            for (int i = 0; i < 99999; i = i + 1) {
                webSocket.send(i + "😀😀😀😀😀😀".repeat(100));
            }
        });

        //也可以使用事件驱动的方式来使用
        ScxEventWebSocket.of(webSocket).onTextMessage((data, s) -> {
            System.out.println("客 : " + data);
        }).start();

    }

}
