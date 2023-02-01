package cool.scx.test;

import cool.scx.beans.ScxBeanFactory;
import cool.scx.mvc.ScxMvc;
import cool.scx.mvc.exception.NoPermException;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.testng.annotations.Test;

import java.util.List;


public class ScxMvcTest {

    public static void main(String[] args) {

//        test0();
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
        new ScxMvc().bindErrorHandler(router);

        router.route("/no-perm").handler(c -> {
            //这里直接抛出会由 ScxMvc 进行处理
            throw new NoPermException();
        });

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080)
                .onSuccess(c -> {
                    System.out.println("http://127.0.0.1:8080/no-perm");
                });
    }

    /**
     * 测试  registerHttpRoutes
     */
    public static void test1() {
        List<Class<?>> classList = List.of(HelloWorldController.class);
        var beanFactory = new ScxBeanFactory().register(classList.toArray(Class[]::new)).refresh().springBeanFactory();

        var vertx = Vertx.vertx();

        var router = Router.router(vertx);

        // 直接将 class 扫描并注册到 router 中 这样可以实现类似 spring mvc 的写法
        // 具体参照 HelloWorldController
        new ScxMvc().bindErrorHandler(router).registerHttpRoutes(router, beanFactory, classList);

        // 原有的并不会收到任何影响
        router.route("/vertx-route").handler(c -> {
            //这里直接抛出会由 ScxMvc 进行处理
            c.response().end("vertx-route");
        });

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080)
                .onSuccess(c -> {
                    System.out.println("http://127.0.0.1:8080/hello");
                    System.out.println("http://127.0.0.1:8080/no-perm");
                    System.out.println("http://127.0.0.1:8080/vertx-route");
                });
    }

}
