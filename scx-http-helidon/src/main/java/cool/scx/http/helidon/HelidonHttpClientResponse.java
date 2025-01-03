package cool.scx.http.helidon;

import cool.scx.http.*;
import io.helidon.webclient.api.HttpClientResponse;

import static cool.scx.http.helidon.HelidonHelper.convertHeaders;

/**
 * HelidonHttpClientResponse
 *
 * @author scx567888
 * @version 0.0.1
 */
class HelidonHttpClientResponse implements ScxHttpClientResponse {

    private final HttpClientResponse response;
    private final HttpStatusCode status;
    private final ScxHttpHeaders headers;
    private final ScxHttpBody body;

    public HelidonHttpClientResponse(HttpClientResponse response, HelidonHttpClient client) {
        this.response = response;
        this.status = HttpStatusCode.of(response.status().code());
        this.headers = convertHeaders(response.headers());
        this.body = new ScxHttpBodyImpl(response.entity().inputStream(), this.headers, client.options().bodyBufferSize());
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
