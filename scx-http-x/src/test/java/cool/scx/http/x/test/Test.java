package cool.scx.http.x.test;

import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.exception.UnauthorizedException;
import cool.scx.http.routing.Router;
import cool.scx.http.x.XHttpServer;
import cool.scx.http.x.XHttpServerOptions;

import static cool.scx.http.method.HttpMethod.GET;
import static cool.scx.http.method.HttpMethod.POST;
import static cool.scx.http.status.HttpStatus.INTERNAL_SERVER_ERROR;
import static cool.scx.http.status.ScxHttpStatusHelper.getReasonPhrase;

public class Test {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var l = System.nanoTime();
        var server = new XHttpServer(new XHttpServerOptions().port(8080));

        var router = Router.of();

//        router.route(-100000).handler(new CorsHandler().addOrigin("http://localhost:18899"));

        router.route().path("/abc").handler(c -> {
            var bytes = c.request().body().asBytes();
            System.out.println(bytes.length);
//            var bbb=c.request().body().asMultiPart();
//            for (var aByte : bbb) {
//                System.out.println(aByte.name()+" "+wrap(()-> aByte.inputStream().readAllBytes().length));
//            }
            c.response().send("12312313");
//            c.next();
        });

        router.route().path("/*").handler(c -> {
            System.out.println(c.request().path());
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
                ctx.response().status(s.status()).send(getReasonPhrase(s.status(), "unknown"));
            } else {
                ctx.response().status(INTERNAL_SERVER_ERROR).send(e.getMessage());
            }
        });

        server.onRequest(router);

        server.start();

        System.out.println("HttpServer 启动完成 !!! 耗时 : " + (System.nanoTime() - l) / 1000_000);
    }

}
