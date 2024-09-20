package cool.scx.http.helidon;

import cool.scx.http.HttpStatusCode;
import cool.scx.http.ScxHttpBody;
import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.ScxHttpHeaders;
import io.helidon.http.ClientResponseHeaders;
import io.helidon.webclient.api.HttpClientResponse;

public class HelidonHttpClientResponse implements ScxHttpClientResponse {

    private final HttpClientResponse request;
    private final HttpStatusCode status;
    private final HelidonHttpHeaders<ClientResponseHeaders> headers;
    private final HelidonHttpBody body;

    public HelidonHttpClientResponse(HttpClientResponse request) {
        this.request = request;
        this.status = HttpStatusCode.of(request.status().code());
        this.headers = new HelidonHttpHeaders<>(request.headers());
        this.body = new HelidonHttpBody(request.entity());
    }

    @Override
    public HttpStatusCode status() {
        return status;
    }

    @Override
    public ScxHttpHeaders headers() {
        return headers;
    }

    @Override
    public ScxHttpBody body() {
        return body;
    }

}
