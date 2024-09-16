package cool.scx.http.helidon;

import cool.scx.http.*;
import io.helidon.http.HeaderNames;
import io.helidon.http.Method;
import io.helidon.webclient.api.WebClient;

import java.io.IOException;
import java.net.URI;

public class HelidonHttpClient implements ScxHttpClient {

    private final WebClient webClient;

    public HelidonHttpClient(ScxHttpClientOptions options) {
        this.webClient = WebClient.builder().build();
    }

    @Override
    public ScxHttpClientResponse request(ScxHttpClientRequest request) throws IOException, InterruptedException {
        var method = Method.create(request.method().value());
        var uri = request.uri().toString();
        var headers = request.headers();
        var r = webClient.method(method);
        r.uri(uri);
        for (var h : headers) {
            r.header(HeaderNames.create(h.getKey().value()), h.getValue());
        }
        if (request.body() != null) {
            r.submit(request.body());
        }
        var res = r.request();
        return new HelidonHttpClientResponse(res);
    }

    @Override
    public ScxClientWebSocket webSocket(URI uri) {
        throw new UnsupportedOperationException();
    }

}
