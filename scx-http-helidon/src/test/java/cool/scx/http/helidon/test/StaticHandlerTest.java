package cool.scx.http.helidon.test;

import cool.scx.http.helidon.HelidonHttpServerOptions;
import cool.scx.http.helidon.HelidonHttpServer;
import cool.scx.http.routing.Router;
import cool.scx.http.routing.handler.StaticHandler;

import java.nio.file.Path;

public class StaticHandlerTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var httpServer = new HelidonHttpServer(new HelidonHttpServerOptions().port(8899));

        var router = Router.of();
        router.route().path("/*").handler(new StaticHandler(Path.of("")));

        httpServer.requestHandler(router);
        httpServer.start();
    }

}
