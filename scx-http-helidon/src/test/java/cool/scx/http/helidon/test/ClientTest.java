package cool.scx.http.helidon.test;

import cool.scx.http.MediaType;
import cool.scx.http.ScxHttpServerOptions;
import cool.scx.http.helidon.HelidonHttpClient;
import cool.scx.http.helidon.HelidonHttpServer;
import cool.scx.http.media.form_params.FormParams;

import java.io.IOException;

import static cool.scx.http.HttpFieldName.ACCEPT;

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
            System.out.println(c.body().asFormParams());
            c.response().send(new Apple("red", "red apple", 99));
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
        var webSocketBuilder = httpClient.webSocket().uri("http://localhost:8990/中:文|路@径/ddd?查询=🎈🎈|🎈#🎃🎃");
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
                //支持 ACCEPT
                .addHeader(ACCEPT, MediaType.APPLICATION_XML.value())
                .uri("http://localhost:8990/中:文|路@径/ddd?查询=🎈🎈|🎈#🎃🎃")
                .send(new FormParams()
                        .add("中文|||/ |||===","嘎 嘎  嘎🧶🧶🛒")
                        .add("🏓🏓🏓","!@#%^%&*%%")
                );
        //可以用不同的方式重复读取
        var apple = response.body().asObject(Apple.class);
        var string = response.body().asString();
        var jsonNode = response.body().asJsonNode();
        System.out.println(string);
    }

}
