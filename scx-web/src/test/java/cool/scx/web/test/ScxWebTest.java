package cool.scx.web.test;

import cool.scx.web.ScxWeb;
import cool.scx.web.ScxWebOptions;
import cool.scx.web.exception.ForbiddenException;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.testng.annotations.Test;

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
        var vertx = Vertx.vertx();

        var router = Router.router(vertx);

        //绑定异常处理器后可以直接再 handler 中抛出异常
        new ScxWeb(new ScxWebOptions().useDevelopmentErrorPage(true)).bindErrorHandler(router);

        router.route("/no-perm").handler(c -> {
            //这里可以直接抛出 异常
            throw new ForbiddenException(new RuntimeException("你没有权限 !!!"));
        });

        router.route("/no-perm2").handler(c -> {
            //或者用这种 vertx 的形式 和上方是一样的
            c.fail(403, new RuntimeException("你没有权限 !!!"));
        });

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080)
                .onSuccess(c -> {
                    System.out.println("http://127.0.0.1:8080/no-perm");
                    System.out.println("http://127.0.0.1:8080/no-perm2");
                });
    }

    /**
     * 测试  registerHttpRoutes
     */
    public static void test1() {

        var vertx = Vertx.vertx();

        var router = Router.router(vertx);

        // 直接将 class 扫描并注册到 router 中 这样可以实现类似 spring mvc 的写法
        // 具体参照 HelloWorldController
        new ScxWeb().bindErrorHandler(router).registerHttpRoutes(router, new HelloWorldController());

        // 原有的并不会收到任何影响
        router.route("/vertx-route").handler(c -> {
            //这里直接抛出会由 ScxWeb 进行处理
            c.response().end("vertx-route");
        });

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8081)
                .onSuccess(c -> {
                    System.out.println("http://127.0.0.1:8081/hello");
                    System.out.println("http://127.0.0.1:8081/no-perm");
                    System.out.println("http://127.0.0.1:8081/vertx-route");
                });
    }

}
