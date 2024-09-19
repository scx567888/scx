package cool.scx.http.helidon.test;

import cool.scx.http.ScxHttpServerOptions;
import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.exception.UnauthorizedException;
import cool.scx.http.helidon.HelidonHttpServer;
import cool.scx.http.routing.Router;

import static cool.scx.http.HttpMethod.GET;
import static cool.scx.http.HttpMethod.POST;
import static cool.scx.http.HttpStatusCode.INTERNAL_SERVER_ERROR;

public class Test {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var l = System.nanoTime();
        var server = new HelidonHttpServer(new ScxHttpServerOptions().setPort(8080));

        var router = Router.of();

        router.route().path("/*").handler(c -> {
            System.out.println(c.request().path().value());
            c.next();
        });

        router.route().path("/hello").method(GET).handler(c -> {
            c.response().send("hello");
        });

        router.route().path("/path-params/:id").method(GET).handler(c -> {
            c.response().send("id : " + c.pathParams().get("id"));
        });

        router.route().path("/401").method(GET).handler(c -> {
            throw new UnauthorizedException();
        });

        router.route().path("/405").method(POST).handler(c -> {
            System.out.println("405");
        });

        router.route().path("/last").method(GET).handler(c -> {
            var r = 1 / 0;
        });

        router.errorHandler((e, ctx) -> {
            if (e instanceof ScxHttpException s) {
                ctx.response().status(s.statusCode()).send(s.statusCode().description());
            } else {
                ctx.response().status(INTERNAL_SERVER_ERROR).send(e.getMessage());
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

}
