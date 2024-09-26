package cool.scx.http.helidon;

import cool.scx.http.HttpStatusCode;
import cool.scx.http.ScxHttpBody;
import cool.scx.http.ScxHttpClientResponse;
import io.helidon.webclient.api.HttpClientResponse;

public class HelidonHttpClientResponse implements ScxHttpClientResponse {

    private final HttpClientResponse response;
    private final HttpStatusCode status;
    private final HelidonHttpClientResponseHeaders headers;
    private final HelidonHttpBody body;

    public HelidonHttpClientResponse(HttpClientResponse response) {
        this.response = response;
        this.status = HttpStatusCode.of(response.status().code());
        this.headers = new HelidonHttpClientResponseHeaders(response.headers());
        this.body = new HelidonHttpBody(response.entity());
    }

    @Override
    public HttpStatusCode status() {
        return status;
    }

    @Override
    public HelidonHttpClientResponseHeaders headers() {
        return headers;
    }

    @Override
    public ScxHttpBody body() {
        return body;
    }

}
