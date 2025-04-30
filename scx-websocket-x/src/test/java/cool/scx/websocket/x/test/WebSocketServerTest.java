package cool.scx.websocket.x.test;

import cool.scx.common.util.$;
import cool.scx.common.util.RandomUtils;
import cool.scx.http.x.HttpServer;
import cool.scx.http.x.HttpServerOptions;
import cool.scx.websocket.ScxServerWebSocketHandshakeRequest;
import cool.scx.websocket.event.ScxEventWebSocket;
import cool.scx.websocket.x.ScxWebSocketClientHelper;
import cool.scx.websocket.x.WebSocketUpgradeHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebSocketServerTest {

    static List<String> eventWebSockets = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        //测试 是否存在 幽灵连接
        test1();
        test2();
    }

    public static void test1() {
        var httpServer = new HttpServer(new HttpServerOptions().addUpgradeHandler(new WebSocketUpgradeHandler()));

        httpServer.onRequest(c -> {
            if (c instanceof ScxServerWebSocketHandshakeRequest wsRequest) {
                System.out.println("收到 WebSocket 握手请求 !!!");
                var scxEventWebSocket = ScxEventWebSocket.of(wsRequest.webSocket());
                var s = RandomUtils.randomString(10);
                eventWebSockets.add(s);
                scxEventWebSocket.onClose((code, reason) -> {
                    eventWebSockets.remove(s);
                    System.err.println("WebSocket closed !!! code : " + code + " reason : " + reason);
                });
                scxEventWebSocket.onError(e -> {
                    System.err.println("WebSocket error : " + e.getMessage());
                });
                scxEventWebSocket.start();
            } else {
                System.out.println("收到 普通 请求 !!!");
                c.response().send(eventWebSockets);
            }
        });

        httpServer.start(8080);

    }

    public static void test2() {
        for (int i = 0; i < 10000; i++) {
            $.sleep(1);
            Thread.ofVirtual().start(() -> {
                try {
                    var scxWebSocket = ScxWebSocketClientHelper.webSocketHandshakeRequest().uri("ws://localhost:8080/websocket").webSocket();
                    $.sleep(1000);
                    scxWebSocket.close();    
                } catch (Exception _) {
                    
                }
            });
        }
    }

}
