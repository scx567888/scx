package cool.scx.http_client.body;

import cool.scx.http_client.ScxHttpClientRequestBody;

import java.net.http.HttpRequest;

public final class EmptyBody implements ScxHttpClientRequestBody {

    @Override
    public HttpRequest.BodyPublisher bodyPublisher(HttpRequest.Builder builder) {
        return HttpRequest.BodyPublishers.noBody();
    }

}
