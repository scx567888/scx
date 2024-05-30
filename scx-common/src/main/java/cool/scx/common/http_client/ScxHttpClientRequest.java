package cool.scx.common.http_client;

import cool.scx.common.http_client.request_body.EmptyBody;
import cool.scx.common.standard.HttpMethod;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;

public class ScxHttpClientRequest {

    private final HttpRequest.Builder builder;

    private ScxHttpClientRequestBody body = new EmptyBody();

    private String method;

    public ScxHttpClientRequest() {
        this.builder = HttpRequest.newBuilder();
    }

    public ScxHttpClientRequest body(ScxHttpClientRequestBody body) {
        this.body = body;
        return this;
    }

    public ScxHttpClientRequest method(HttpMethod method) {
        this.method = method.name();
        return this;
    }

    public ScxHttpClientRequest method(String method) {
        this.method = method;
        return this;
    }

    public ScxHttpClientRequest timeout(Duration duration) {
        builder.timeout(duration);
        return this;
    }

    public ScxHttpClientRequest uri(URI uri) {
        builder.uri(uri);
        return this;
    }

    public ScxHttpClientRequest uri(String uri) {
        return this.uri(URI.create(uri));
    }

    public ScxHttpClientRequest setHeader(String name, String value) {
        builder.setHeader(name, value);
        return this;
    }

    public ScxHttpClientRequest addHeader(String name, String value) {
        builder.header(name, value);
        return this;
    }

    public HttpRequest createHttpRequest() {
        return builder.method(method, body.bodyPublisher(builder)).build();
    }

}
