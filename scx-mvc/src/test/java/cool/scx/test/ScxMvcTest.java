package cool.scx.test;

import cool.scx.beans.ScxBeanFactory;
import cool.scx.mvc.ScxMvc;
import cool.scx.mvc.ScxMvcOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import org.testng.annotations.Test;


public class ScxMvcTest {

    public static void main(String[] args) {

        test0();

    }


    @Test
    public static void test0() {
        var vertx = Vertx.vertx();
        var beanFactory = new ScxBeanFactory().register(HelloWorldController.class).refresh();
        var scxMvc = new ScxMvc(new ScxMvcOptions().addClass(HelloWorldController.class), vertx, beanFactory);
        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(scxMvc.scxHttpRouter().vertxRouter()).webSocketHandler(scxMvc.scxWebSocketRouter());
        httpServer.listen(8080).onSuccess(c -> {
            System.out.println("http://127.0.0.1:8080/hello");
        });
    }

}
