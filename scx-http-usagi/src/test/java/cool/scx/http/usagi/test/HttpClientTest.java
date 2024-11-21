package cool.scx.http.usagi.test;

import cool.scx.http.usagi.UsagiHttpClient;

import static cool.scx.http.HttpMethod.POST;

public class HttpClientTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
//        UsagiTest.test1();

        var usagiHttpClient = new UsagiHttpClient();
        var send = usagiHttpClient.request()
                .uri("http://localhost:8899/中文路径😎😎😎😎?a=1&b=llll")
                .addHeader("a", "b")
                .method(POST)
                .send("测试内容 😂😂😂😂😂😂😂");

        String string = send.body().asString();
        System.out.println(string);
    }

}
