package cool.scx.http.x.test;

import cool.scx.http.x.XHttpClient;

import static cool.scx.http.method.HttpMethod.POST;

public class HttpClientTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        XTest.test1();

        var client = new XHttpClient();
        var send = client.request()
                .uri("http://localhost:8899/中文路径😎😎😎😎?a=1&b=llll")
                .addHeader("a", "b")
                .method(POST)
                .send("测试内容 😂😂😂😂😂😂😂");

        String string = send.body().asString();
        System.out.println(string);
    }

}

