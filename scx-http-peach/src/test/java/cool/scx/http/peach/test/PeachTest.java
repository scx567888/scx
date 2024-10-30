package cool.scx.http.peach.test;

import cool.scx.http.ScxHttpServerOptions;
import cool.scx.http.ScxServerWebSocketHandshakeRequest;
import cool.scx.http.peach.PeachHttpServer;
import cool.scx.net.tls.TLS;

import java.nio.file.Path;

public class PeachTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var httpServer = new PeachHttpServer(new ScxHttpServerOptions().port(8899));
        httpServer.requestHandler(c -> {
            System.out.println(c.method() + " " + c.uri() + " -> " + c.body().asString());
            //通过 c 的类型判断是不是 websocket 连接
            if (c instanceof ScxServerWebSocketHandshakeRequest w) {
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
