package cool.scx.http.peach.test;

import cool.scx.http.ScxHttpServerOptions;
import cool.scx.http.peach.PeachHttpServer;

public class PeachTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var httpServer = new PeachHttpServer(new ScxHttpServerOptions().port(8899));
        httpServer.requestHandler(c -> {
            System.out.println(c.body().asString());
            c.response().send("123");
        });
        httpServer.start();
        System.out.println("启动完成 !!! 端口号 : " + httpServer.port());
    }

}
