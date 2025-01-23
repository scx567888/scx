package cool.scx.socket2.test;

import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;
import cool.scx.http.x.XHttpServer;
import cool.scx.http.x.XHttpServerOptions;
import cool.scx.socket2.ScxSocketServer;
import org.testng.annotations.Test;

public class ScxSocketServerTest extends InitLogger {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        //创建服务器
        var scxSocketServer = new ScxSocketServer();

        scxSocketServer.onConnect(clientContent -> {
            System.out.println("连接了"+clientContent.clientID());
        });

        //使用 httpServer
        new XHttpServer(new XHttpServerOptions().port(8990))
                .onRequest(c -> {
                    if (c instanceof ScxServerWebSocketHandshakeRequest s) {
                        scxSocketServer.call(s);
                    }
                })
                .start();

    }

}
