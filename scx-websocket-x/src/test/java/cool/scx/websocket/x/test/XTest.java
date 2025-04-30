package cool.scx.websocket.x.test;

import cool.scx.http.x.XHttpServer;
import cool.scx.http.x.XHttpServerOptions;
import cool.scx.websocket.ScxServerWebSocketHandshakeRequest;
import cool.scx.websocket.handler.ScxEventWebSocket;
import cool.scx.websocket.x.WebSocketUpgradeHandler;

public class XTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var httpServer = new XHttpServer(new XHttpServerOptions().addUpgradeHandlerList(
                new WebSocketUpgradeHandler()
        ));
        httpServer.onRequest(c -> {
            System.out.println(c.method() + " " + c.uri() + " -> " + c.body().asString());
            //通过 c 的类型判断是不是 websocket 连接
            if (c instanceof ScxServerWebSocketHandshakeRequest w) {
                System.out.println("这是 websocket handshake");
                w.webSocket().send("hello");
                ScxEventWebSocket.of(w.webSocket()).onTextMessage((s, _) -> {
                    System.out.println("收到消息 :" + s);
                }).start();

            } else {
                // c.response().setHeader("transfer-encoding", "chunked");
                c.response().send("123");
            }
        });
        httpServer.start(8899);
        System.out.println("启动完成 !!! 端口号 : " + httpServer.localAddress().getPort());
    }

}
