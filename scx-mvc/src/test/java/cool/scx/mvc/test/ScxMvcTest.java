package cool.scx.mvc.test;

import cool.scx.mvc.ScxMvc;
import cool.scx.mvc.ScxMvcOptions;
import cool.scx.mvc.exception.ForbiddenException;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.testng.annotations.Test;

import java.util.List;


public class ScxMvcTest {

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
        new ScxMvc(new ScxMvcOptions().useDevelopmentErrorPage(true)).bindErrorHandler(router);

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
        List<Class<?>> classList = List.of(HelloWorldController.class);
        var beanFactory = getBeanFactory(classList);

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
                .listen(8081)
                .onSuccess(c -> {
                    System.out.println("http://127.0.0.1:8081/hello");
                    System.out.println("http://127.0.0.1:8081/no-perm");
                    System.out.println("http://127.0.0.1:8081/vertx-route");
                });
    }

    private static DefaultListableBeanFactory getBeanFactory(List<Class<?>> classList) {
        var beanFactory = new DefaultListableBeanFactory();
        //这里添加一个 bean 的后置处理器以便可以使用 @Autowired 注解
        var beanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        beanPostProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(beanPostProcessor);

        //设置是否允许循环依赖 (默认禁止循环依赖)
        beanFactory.setAllowCircularReferences(true);

        for (var c : classList) {
            var beanDefinition = new AnnotatedGenericBeanDefinition(c);
            //这里是为了兼容 spring context 的部分注解
            AnnotationConfigUtils.processCommonDefinitionAnnotations(beanDefinition);
            beanFactory.registerBeanDefinition(c.getName(), beanDefinition);
        }
        return beanFactory;
    }

}
