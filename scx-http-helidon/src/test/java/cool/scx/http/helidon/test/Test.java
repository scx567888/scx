package cool.scx.http.helidon.test;

import cool.scx.http.ScxHttpServerOptions;
import cool.scx.http.helidon.HelidonHttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;

public class Test {

    public static void main(String[] args) {
        test1();
        test2();
    }

    public static void test1() {
        var l = System.nanoTime();
        Vertx vertx = Vertx.vertx();

        var server = vertx.createHttpServer(new HttpServerOptions().setPort(8081));

        server.requestHandler(c -> {
            var response = c.response();
            response.end("响应数据!!!");
        });

        server.listen().onSuccess(c -> {
            System.out.println("VertxHttpServer 启动完成 !!! 耗时 : " + (System.nanoTime() - l) / 1000_000);
        });
    }

    public static void test2() {
        var l = System.nanoTime();
        var server = new HelidonHttpServer(new ScxHttpServerOptions().setPort(8080));

        server.requestHandler(c -> {
            var response = c.response();
            response.send("响应数据");
        });

        server.start();

        System.out.println("HelidonHttpServer 启动完成 !!! 耗时 : " + (System.nanoTime() - l) / 1000_000);
    }

}
