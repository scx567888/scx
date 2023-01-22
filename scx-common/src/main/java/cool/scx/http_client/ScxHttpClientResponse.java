package cool.scx.http_client;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class ScxHttpClientResponse {

    private final HttpResponse<byte[]> response;

    private final ScxHttpClientResponseBody body;

    public ScxHttpClientResponse(HttpResponse<byte[]> response) {
        this.response = response;
        this.body = new ScxHttpClientResponseBody(response.body());
    }

    public ScxHttpClientResponseBody body() {
        return body;
    }

    public Optional<SSLSession> sslSession() {
        return response.sslSession();
    }

    public int statusCode() {
        return response.statusCode();
    }

    public HttpRequest request() {
        return response.request();
    }

    public HttpHeaders headers() {
        return response.headers();
    }

    public URI uri() {
        return response.uri();
    }

    public HttpClient.Version version() {
        return response.version();
    }

}
