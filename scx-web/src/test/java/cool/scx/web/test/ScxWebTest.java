package cool.scx.web.test;

import cool.scx.http.exception.ForbiddenException;
import cool.scx.http.routing.Route;
import cool.scx.http.routing.Router;
import cool.scx.http.x.XHttpServer;
import cool.scx.http.x.XHttpServerOptions;
import cool.scx.web.ScxWeb;
import org.testng.annotations.Test;

import static cool.scx.http.status.HttpStatus.FORBIDDEN;

public class ScxWebTest {

    public static void main(String[] args) {
        test0();
        test1();
    }

    /// 测试 bindErrorHandler
    @Test
    public static void test0() {
        var httpServer = new XHttpServer();

        var router = Router.of();

        router.addRoute(Route.of().path("/no-perm").handler(c -> {
            //这里可以直接抛出 异常
            throw new ForbiddenException(new RuntimeException("你没有权限 !!!"));
        }));

        router.addRoute(Route.of().path("/no-perm2").handler(c -> {
            //或者用这种 httpServer 的形式 和上方是一样的
            c.response().status(FORBIDDEN).send("Error");
        }));

        httpServer.onRequest(router).start(8080);

        for (var route : router.getRoutes()) {
            System.out.println("http://127.0.0.1:" + httpServer.localAddress().getPort() + route.path());
        }

    }

    /// 测试  registerHttpRoutes
    public static void test1() {

        var httpServer = new XHttpServer();

        var router = Router.of();

        // 直接将 class 扫描并注册到 router 中 这样可以实现类似 spring mvc 的写法
        // 具体参照 HelloWorldController
        new ScxWeb().registerHttpRoutes(router, new HelloWorldController());

        // 原有的并不会收到任何影响
        router.addRoute(Route.of().path("/my-route").handler(c -> {
            //这里直接抛出会由 ScxWeb 进行处理
            c.response().send("my-route");
        }));

        httpServer.onRequest(router).start(8081);

        for (var route : router.getRoutes()) {
            System.out.println("http://127.0.0.1:" + httpServer.localAddress().getPort() + route.path());
        }

    }

}
