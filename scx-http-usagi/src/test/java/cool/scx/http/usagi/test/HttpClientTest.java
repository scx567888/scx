package cool.scx.http.usagi.test;

import cool.scx.http.usagi.UsagiHttpClient;

import static cool.scx.http.HttpMethod.GET;

public class HttpClientTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var usagiHttpClient = new UsagiHttpClient();
        var send = usagiHttpClient.request().uri("http://localhost:8899/aaaaa").addHeader("a", "b").method(GET).send();
        String string = send.body().asString();
        System.out.println(string);
    }

}
