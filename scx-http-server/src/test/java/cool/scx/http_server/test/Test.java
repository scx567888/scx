package cool.scx.http_server.test;

import cool.scx.http_server.ScxHttpResponse;
import cool.scx.http_server.ScxHttpServer;
import cool.scx.http_server.ScxHttpServerOptions;
import cool.scx.http_server.ScxTCPServer;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.WebServerConfig;
import io.helidon.webserver.http.*;
import io.helidon.webserver.http.Handler;
import io.vertx.core.*;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.io.InputStream;
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


    public static void main123(String[] args) {
        Vertx vertx = Vertx.vertx();

        var server = vertx.createHttpServer(new HttpServerOptions().setPort(8888));

        server.requestHandler(c -> {
            HttpServerResponse response = c.response();
            Thread.ofVirtual().start(() -> {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                response.end("8888888");
            });

        });

        server.listen();
    }

    public static void maina(String[] args) {
        ScxTCPServer server = ScxTCPServer.create();
        server.connectHandler(c -> {
            try {
                InputStream inputStream = c.getInputStream();
                byte[] bytes = inputStream.readAllBytes();
                System.out.println(new String(bytes, StandardCharsets.UTF_8));
                c.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        System.out.println(123);
    }


    public static void main(String[] args) {
        WebServer webServer = WebServer.create(
                WebServerConfig
                        .builder()
                        .port(3215)
                        .routing(
                                HttpRouting.builder()
                                        .route(
                                                HttpRoute
                                                        .builder()
                                                        .handler((req, res) -> {
                                                            System.out.println();
                                                            res.send("asdasdasd");
                                                        })
                                                        .build())
                        )
                        .buildPrototype()
        );
        webServer.start();
    }

}
