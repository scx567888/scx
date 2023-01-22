package cool.scx.http_client;

import java.net.http.HttpRequest;

public interface ScxHttpClientRequestBody {

    HttpRequest.BodyPublisher bodyPublisher(HttpRequest.Builder builder);

}