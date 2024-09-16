package cool.scx.http.helidon.test;

import cool.scx.http.HttpMethod;
import cool.scx.http.ScxHttpClientOptions;
import cool.scx.http.ScxHttpClientRequest;
import cool.scx.http.helidon.HelidonHttpClient;

import java.io.IOException;

public class ClientTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        var httpClient = new HelidonHttpClient(new ScxHttpClientOptions());
        var response = httpClient.request(ScxHttpClientRequest.of()
                .method(HttpMethod.GET)
                .uri("http://www.baidu.com"));
        var string = response.body().asString();
        System.out.println(string);
    }

}
