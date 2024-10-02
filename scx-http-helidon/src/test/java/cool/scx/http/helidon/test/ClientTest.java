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
        httpServer.requestHandler(c -> {
            System.out.println(c.uri());
            c.response().send("Hi Client !!!");
        });
        httpServer.webSocketHandler(c -> {
            System.out.println(c.uri());
            c.onTextMessage(t -> {
                System.out.println(t);
            });
            c.send("Hi WS Client !!!");
        });
        httpServer.start();
    }

    public static void test2() {
        var httpClient = new HelidonHttpClient();
        var webSocketBuilder = httpClient.webSocket().uri("http://localhost:8990/中:文|路径/ddd?查询=🎈🎈|🎈");
        webSocketBuilder.onConnect(webSocket -> {
            webSocket.onTextMessage(t -> {
                System.out.println(t);
            });
            webSocket.send("Hi Server !!!");
        });
        webSocketBuilder.connect();
    }

    public static void test3() {
        var httpClient = new HelidonHttpClient();
        var response = httpClient.request()
                .uri("http://localhost:8990/中:文|路径/ddd?查询=🎈🎈|🎈")
                .send();
        var string = response.body().asString();
        System.out.println(string);
    }

}
