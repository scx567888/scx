package cool.scx.http.usagi.test;

import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;
import cool.scx.http.usagi.UsagiHttpServer;
import cool.scx.http.usagi.UsagiHttpServerOptions;

public class UsagiTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var httpServer = new UsagiHttpServer(new UsagiHttpServerOptions().port(8899));
        httpServer.requestHandler(c -> {
            System.out.println(c.method() + " " + c.uri() + " -> " + c.body().asString());
            //通过 c 的类型判断是不是 websocket 连接
            if (c instanceof ScxServerWebSocketHandshakeRequest w) {
                System.out.println("这是 websocket handshake");
                var d = w.webSocket();
                d.onTextMessage(s -> {
                    System.out.println("收到消息 :" + s);
                });
                d.send("hello");
            } else {
                c.response().send("123");
            }
        });
        httpServer.start();
        System.out.println("启动完成 !!! 端口号 : " + httpServer.port());
    }

}
