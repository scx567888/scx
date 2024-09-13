package cool.scx.http.helidon.test;

import cool.scx.http.HttpMethod;
import cool.scx.http.ScxHttpServerOptions;
import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.exception.UnauthorizedException;
import cool.scx.http.helidon.HelidonHttpServer;
import cool.scx.http.routing.Route;
import cool.scx.http.routing.Router;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;

import static cool.scx.http.HttpMethod.GET;
import static cool.scx.http.HttpStatusCode.INTERNAL_SERVER_ERROR;

public class Test {

    public static void main(String[] args) {
        test1();
        test2();
    }

    public static void test1() {
        var l = System.nanoTime();
        var server = new HelidonHttpServer(new ScxHttpServerOptions().setPort(8080));

        var router = new Router();

        router.addRoute(new Route().path("/*").handler(c -> {
            System.out.println(c.request().path().path());
            c.next();
        }));

        router.addRoute(new Route().path("/hello").method(GET).handler(c -> {
            c.response().send("hello");
        }));

        router.addRoute(new Route().path("/path-params/:id").method(GET).handler(c -> {
            c.response().send("id : " + c.pathParams().get("id"));
        }));

        router.addRoute(new Route().path("/401").method(GET).handler(c -> {
            throw new UnauthorizedException();
        }));

        router.addRoute(new Route().path("/405").method(HttpMethod.POST).handler(c -> {
            System.out.println("405");
        }));

        router.addRoute(new Route().path("/last").method(GET).handler(c -> {
            c.response().send("last");
        }));

        router.exceptionHandler((e, ctx) -> {
            if (e instanceof ScxHttpException s) {
                ctx.response().setStatusCode(s.statusCode()).send(s.statusCode().description());
            } else {
                ctx.response().setStatusCode(INTERNAL_SERVER_ERROR).send(e.getMessage());
            }
        });

        server.requestHandler(router).webSocketHandler(c -> {

            c.onTextMessage(s -> {
                System.out.println("Text Message: " + s);
            });

            c.send("This is Server", false);

        });

        server.start();

        System.out.println("HelidonHttpServer 启动完成 !!! 耗时 : " + (System.nanoTime() - l) / 1000_000);
    }

    public static void test2() {
        var l = System.nanoTime();
        Vertx vertx = Vertx.vertx();

        var server = vertx.createHttpServer(new HttpServerOptions().setPort(8081));

        server.requestHandler(c -> {
            var response = c.response();
            response.end("响应数据!!!");
        }).webSocketHandler(c -> {

            c.textMessageHandler(s -> {
                System.out.println("Text Message: " + s);
            });

            c.writeTextMessage("This is Server");

        });

        server.listen().onSuccess(c -> {
            System.out.println("VertxHttpServer 启动完成 !!! 耗时 : " + (System.nanoTime() - l) / 1000_000);
        });
    }

}
