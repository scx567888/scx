package cool.scx.http.x.test;

import cool.scx.http.x.HttpServer;

import java.io.IOException;

public class XTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    public static void test1() throws IOException {
        var httpServer = new HttpServer();
        httpServer.onRequest(c -> {
            var cacheBody = c.body().asGzipBody().asCacheBody();
            var bodyStr1 = cacheBody.asString();
            var bodyStr2 = cacheBody.asString();
            System.out.println(c.method() + " " + c.uri() + " -> " + bodyStr1 + " " + bodyStr1.equals(bodyStr2));
            // c.response().setHeader("transfer-encoding", "chunked");
            c.response().sendGzip().send("123");
        });
        httpServer.start(8899);
        System.out.println("启动完成 !!! 端口号 : " + httpServer.localAddress().getPort());
    }

}
