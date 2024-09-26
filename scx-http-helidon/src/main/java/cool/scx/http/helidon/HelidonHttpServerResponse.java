package cool.scx.http.helidon;

import cool.scx.http.HttpStatusCode;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.ScxHttpServerResponseHeaders;
import io.helidon.webserver.http.RoutingResponse;

import java.io.OutputStream;

/**
 * HelidonHttpServerResponse
 */
class HelidonHttpServerResponse implements ScxHttpServerResponse {

    private final RoutingResponse response;
    private final HelidonHttpServerResponseHeaders headers;

    public HelidonHttpServerResponse(RoutingResponse response) {
        this.response = response;
        this.headers = new HelidonHttpServerResponseHeaders(response.headers());
    }

    @Override
    public HttpStatusCode status() {
        return HttpStatusCode.of(this.response.status().code());
    }

    @Override
    public ScxHttpServerResponseHeaders headers() {
        return headers;
    }

    @Override
    public ScxHttpServerResponse status(HttpStatusCode code) {
        this.response.status(code.code());
        return this;
    }

    @Override
    public OutputStream outputStream() {
        return this.response.outputStream();
    }

    @Override
    public void send() {
        this.response.send();
    }

    @Override
    public void send(byte[] data) {
        this.response.send(data);
    }

    @Override
    public void send(String data) {
        this.response.send(data);
    }

    @Override
    public void send(Object data) {
        this.response.send(data);
    }

    @Override
    public boolean isClosed() {
        return this.response.isSent();
    }

}
