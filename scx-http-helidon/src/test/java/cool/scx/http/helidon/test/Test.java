package cool.scx.http.helidon.test;

import cool.scx.http.*;
import cool.scx.http.helidon.HelidonHttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;

import java.nio.charset.StandardCharsets;

import static cool.scx.http.HttpFieldName.COOKIE;

public class Test {

    public static void main2(String[] args) {
        Vertx vertx = Vertx.vertx();

        var server = vertx.createHttpServer(new HttpServerOptions().setPort(8888));

        server.requestHandler(c -> {
            HttpServerResponse response = c.response();
            response.end("8888888");
        });

        server.listen();
    }

    public static void main(String[] args) {
        var server = new HelidonHttpServer(new ScxHttpServerOptions().setPort(8080));
        var s = new ScxRouter();
        s.addRoute(new ScxRoute().handler((c) -> {
            ScxHttpHeaders headers = c.request().headers();
            String s1 = headers.get(COOKIE);
            for (var header : headers) {
                System.out.println(header.getKey() + " " + header.getValue());
            }
//            $.sleep(1000);
            ScxHttpServerRequest request = c.request();
            ScxHttpBody body = request.body();
//            var string = body.asFormData();
            var stringa = body.asString();
            System.out.println(stringa);
            ScxHttpServerResponse response = request.response();
            response.send("司昌旭".getBytes(StandardCharsets.UTF_8));
        }));
        server.requestHandler(s).start();
        System.out.println(123);
    }

}
