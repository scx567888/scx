package cool.scx.http.helidon.test;

import cool.scx.http.ScxHttpServerOptions;
import cool.scx.http.helidon.HelidonHttpClient;
import cool.scx.http.helidon.HelidonHttpServer;

import java.io.IOException;

public class ClientTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        test1();
        test2();
        test3();
    }

    public static void test1() throws IOException, InterruptedException {
        var httpServer = new HelidonHttpServer(new ScxHttpServerOptions().setPort(8990));
        httpServer.webSocketHandler(c -> {
            System.out.println(c.uri());
            c.onTextMessage(t -> {
                System.out.println(t);
            });
            c.send("Hi Client !!!");
        });
        httpServer.start();
    }

    public static void test2() {
        var httpClient = new HelidonHttpClient();
        var webSocketBuilder = httpClient.webSocket().uri("http://localhost:89901/test");
        webSocketBuilder.onConnect(webSocket -> {
            webSocket.onTextMessage(t -> {
                System.out.println(t);
            });
            webSocket.send("Hi Server !!!");
        });
        webSocketBuilder.connect();
    }

    public static void test3() throws IOException, InterruptedException {
        var httpClient = new HelidonHttpClient();
        var response = httpClient.request()
                .uri("http://www.baidu.com")
                .request();
        var string = response.body().asString();
        System.out.println(string.length());
    }

}
