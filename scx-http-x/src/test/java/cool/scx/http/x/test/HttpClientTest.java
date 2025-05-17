package cool.scx.http.x.test;

import cool.scx.http.x.HttpClient;

import java.io.IOException;

import static cool.scx.http.method.HttpMethod.POST;

public class HttpClientTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    public static void test1() throws IOException {
        XTest.test1();

        var client = new HttpClient();
        var send = client.request()
                .uri("http://localhost:8899/中文路径😎😎😎😎?a=1&b=llll")
                .addHeader("a", "b")
                .method(POST)
                .sendGzip()
                .send("测试内容 😂😂😂😂😂😂😂");

        var string = send.body().asGzipBody().asString();
        System.out.println(string);
    }

}

