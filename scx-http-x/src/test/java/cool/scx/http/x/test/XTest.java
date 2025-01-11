package cool.scx.http.x.test;

import cool.scx.http.x.XHttpServer;
import cool.scx.http.x.XHttpServerOptions;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;

public class XTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var httpServer = new XHttpServer(new XHttpServerOptions().port(8899));
        httpServer.onRequest(c -> {
            System.out.println(c.method() + " " + c.uri() + " -> " + c.body().asString());
            //通过 c 的类型判断是不是 websocket 连接
            if (c instanceof ScxServerWebSocketHandshakeRequest w) {
                System.out.println("这是 websocket handshake");
                var d = w.webSocket();
                d.onTextMessage((s, _) -> {
                    System.out.println("收到消息 :" + s);
                });
                d.send("hello");
            } else {
                c.response().send("123");
            }
        });
        httpServer.start();
        System.out.println("启动完成 !!! 端口号 : " + httpServer.localAddress().getPort());
    }

}
