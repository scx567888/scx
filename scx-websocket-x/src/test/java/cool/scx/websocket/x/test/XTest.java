package cool.scx.websocket.x.test;

import cool.scx.http.x.HttpServer;
import cool.scx.http.x.HttpServerOptions;
import cool.scx.websocket.ScxServerWebSocketHandshakeRequest;
import cool.scx.websocket.event.ScxEventWebSocket;
import cool.scx.websocket.x.WebSocketUpgradeHandler;

import java.io.IOException;

public class XTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    public static void test1() throws IOException {
        var httpServer = new HttpServer(new HttpServerOptions().addUpgradeHandler(
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
