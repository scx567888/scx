package cool.scx.socket.test;

import com.fasterxml.jackson.core.type.TypeReference;
import cool.scx.http.ScxHttpServerOptions;
import cool.scx.http.helidon.HelidonHttpServer;
import cool.scx.socket.ScxSocketServer;
import org.testng.annotations.Test;

import java.util.List;

public class ScxSocketServerTest extends InitLogger {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        //创建服务器
        var scxSocketServer = new ScxSocketServer();

        scxSocketServer.onConnect(clientContent -> {

            clientContent.onMessage((m) -> {
                System.out.println("客户端发来的消息 : " + m);
            });

            clientContent.onEvent("a", (r) -> {
                var m = r.payload(new TypeReference<User>() {});
                System.out.println("客户端发来的事件 : " + m);
                clientContent.sendEvent("b", m);
            });

            clientContent.onClose((i, s) -> {
                System.out.println("close");
            });

            clientContent.onError(e -> {
                e.printStackTrace();
            });

            //响应方法 
            clientContent.onEvent("aaa", (r) -> {
                r.response(r.payload() + "🙄");
            });

            //相同事件名称 会覆盖
            clientContent.onEvent("aaa", (r) -> {
                r.response(r.payload() + "😆");
            });

            clientContent.onEvent("ss", (r) -> {
                var c = r.payload(new TypeReference<List<User>>() {});
                c.add(new User("Tom", 88));
                r.response(c);
            });

        });

        //使用 vertx 的 httpServer
        new HelidonHttpServer(new ScxHttpServerOptions().port(8990))
                .webSocketHandler(scxSocketServer::call)
                .start();

    }

}
