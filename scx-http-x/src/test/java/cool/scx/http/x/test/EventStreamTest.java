package cool.scx.http.x.test;

import cool.scx.common.util.$;
import cool.scx.http.media.event_stream.SseEvent;
import cool.scx.http.x.XHttpClient;
import cool.scx.http.x.XHttpServer;
import cool.scx.http.x.XHttpServerOptions;

import java.io.IOException;

public class EventStreamTest {

    public static void main(String[] args) {
        test1();
        test2();
    }

    public static void test1() {
        var httpServer = new XHttpServer(new XHttpServerOptions().port(8080));
        httpServer.onRequest(c -> {
            System.out.println("连接了");
            c.response().setHeader("Access-Control-Allow-Origin", "*");
            var eventStream = c.response().sendEventStream();
            try (eventStream) {
                for (int i = 0; i < 100; i++) {
                    eventStream.send(SseEvent.of("hello" + i).id("123").event("这是事件" + i).comment("这是注释"));
                    $.sleep(10);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("全部发送完成");
        });
        httpServer.start();

    }

    public static void test2() {
        var client = new XHttpClient();
        var eventStream = client.request().uri("http://127.0.0.1:8080").send().body().asEventStream();
        eventStream.onEvent(event -> {
            System.err.println(event.event() + " " + event.data());
        });
        eventStream.start();
    }

}
