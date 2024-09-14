package cool.scx.web.test;

import cool.scx.http.ScxHttpServerOptions;
import cool.scx.http.exception.ForbiddenException;
import cool.scx.http.helidon.HelidonHttpServer;
import cool.scx.http.routing.Route;
import cool.scx.http.routing.Router;
import cool.scx.web.ScxWeb;
import cool.scx.web.ScxWebOptions;
import org.testng.annotations.Test;

import static cool.scx.http.HttpStatusCode.FORBIDDEN;

public class ScxWebTest {

    public static void main(String[] args) {
        test0();
        test1();
    }

    /**
     * 测试 bindErrorHandler
     */
    @Test
    public static void test0() {
        var vertx = new HelidonHttpServer(new ScxHttpServerOptions().setPort(8080));

        var router = new Router();

        //绑定异常处理器后可以直接再 handler 中抛出异常
        new ScxWeb(new ScxWebOptions().useDevelopmentErrorPage(true)).bindErrorHandler(router);

        router.addRoute(Route.of().path("/no-perm").handler(c -> {
            //这里可以直接抛出 异常
            throw new ForbiddenException(new RuntimeException("你没有权限 !!!"));
        }));

        router.addRoute(Route.of().path("/no-perm2").handler(c -> {
            //或者用这种 vertx 的形式 和上方是一样的
            c.response().setStatusCode(FORBIDDEN).send("Error");
        }));

        vertx.requestHandler(router).start();

        System.out.println("http://127.0.0.1:8080/no-perm");
        System.out.println("http://127.0.0.1:8080/no-perm2");

    }

    /**
     * 测试  registerHttpRoutes
     */
    public static void test1() {

        var vertx = new HelidonHttpServer(new ScxHttpServerOptions().setPort(8081));

        var router = new cool.scx.http.routing.Router();

        // 直接将 class 扫描并注册到 router 中 这样可以实现类似 spring mvc 的写法
        // 具体参照 HelloWorldController
        new ScxWeb().bindErrorHandler(router).registerHttpRoutes(router, new HelloWorldController());

        // 原有的并不会收到任何影响
        router.addRoute(Route.of().path("/vertx-route").handler(c -> {
            //这里直接抛出会由 ScxWeb 进行处理
            c.response().send("vertx-route");
        }));

        vertx.requestHandler(router).start();

        System.out.println("http://127.0.0.1:8081/hello");
        System.out.println("http://127.0.0.1:8081/no-perm");
        System.out.println("http://127.0.0.1:8081/vertx-route");
    }

}
