package cool.scx.common.http_client;

import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.Builder;

public interface ScxHttpClientRequestBody {

    BodyPublisher bodyPublisher(Builder builder);

}
