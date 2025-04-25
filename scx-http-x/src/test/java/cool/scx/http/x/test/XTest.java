package cool.scx.http.x.test;

import cool.scx.http.x.XHttpServer;
import cool.scx.http.x.XHttpServerOptions;

public class XTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var httpServer = new XHttpServer(new XHttpServerOptions().port(8899));
        httpServer.onRequest(c -> {
            System.out.println(c.method() + " " + c.uri() + " -> " + c.body().asGzipBody().asString());
            // c.response().setHeader("transfer-encoding", "chunked");
            c.response().sendGzip().send("123");
        });
        httpServer.start();
        System.out.println("启动完成 !!! 端口号 : " + httpServer.localAddress().getPort());
    }

}
