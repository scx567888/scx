package cool.scx.http.helidon.test;

import cool.scx.http.*;
//import cool.scx.http_server.impl.helidon.HelidonHttpServer;
import cool.scx.http.helidon.HelidonHttpServer;
import io.vertx.core.*;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;

import java.nio.charset.StandardCharsets;

public class Test {

    public static void main1(String[] args) {
        Vertx vertx = Vertx.vertx();

        var server = vertx.createHttpServer();

        server.requestHandler(c -> {
            System.out.println(Thread.currentThread());
            var response = c.response();
            var body = c.body();
//            System.out.println(new String(body, StandardCharsets.UTF_8));
//            response.write("12345".getBytes(StandardCharsets.UTF_8));
//            response.end("888888".getBytes(StandardCharsets.UTF_8));
            System.out.println(567);
        });
        System.out.println(888);
        server.listen();
        System.out.println(999);

    }

    public static void main11(String[] args) {
        Vertx vertx = Vertx.vertx();

        var server = vertx.createHttpServer(new HttpServerOptions().setPort(3306));

        server.requestHandler(c -> {
            HttpServerResponse response = c.response();
            response.end("8888888");
        });

        server.listen();
    }

    public static void main12(String[] args) throws InterruptedException {
        AbstractVerticle verticle = new AbstractVerticle() {
            @Override
            public void start(Promise<Void> startPromise) {
                var server = vertx.createHttpServer(new HttpServerOptions().setPort(8888));

                server.requestHandler(c -> {
                    HttpServerResponse response = c.response();
//                    $.sleep(1000);
//                    Thread.ofVirtual().start(()->{

//                        $.sleep(1000);
                    System.out.println(Thread.currentThread());

                    response.end("8888888");
//                    });

                });

                server.listen().onSuccess(c -> {
                    startPromise.complete();
                }).onFailure(startPromise::fail);
            }
        };

        Vertx vertx = Vertx.vertx();
// Run the verticle a on virtual thread
        vertx.deployVerticle(verticle, new DeploymentOptions().setThreadingModel(ThreadingModel.VIRTUAL_THREAD));
        Thread.sleep(999999999L);
    }


    public static void maina(String[] args) {
        Vertx vertx = Vertx.vertx();

        var server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));


        server.requestHandler(c -> {
            c.version();
            HttpServerResponse response = c.response();
            response.end("司昌旭");
        });

        server.listen();
    }

    public static void main(String[] args) {
        var server = new HelidonHttpServer(new ScxHttpServerOptions().setPort(8080));
        var s=new ScxRouter();
        s.addRoute(new ScxRoute().handler((c)->{
            ScxHttpHeaders headers = c.request().headers();
            for (var header : headers) {
                System.out.println(header.getKey()+" "+header.getValue());
            }
//            $.sleep(1000);
            ScxHttpServerRequest request = c.request();
            ScxHttpBody body = request.body();
//            var string = body.asFormData();
            var stringa = body.asString();
            System.out.println(stringa);
            ScxHttpServerResponse response = request.response();
            response.send("司昌旭".getBytes(StandardCharsets.UTF_8));
        }));
        server.requestHandler(s).start();
        System.out.println(123);
    }

}
