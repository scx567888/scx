package cool.scx.common.http_client.test;

import cool.scx.common.http_client.ScxHttpClientHelper;

import java.io.IOException;

public class ScxHttpClientTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        test1();
    }

    public static void test1() throws IOException, InterruptedException {
        var response = ScxHttpClientHelper.get("https://www.baidu.com");
        var body = response.body();
        System.out.println(body.toString());
        System.out.println(body.toString());
    }

}
