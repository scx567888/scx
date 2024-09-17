package cool.scx.http.helidon;

import cool.scx.http.HttpStatusCode;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.ScxHttpServerResponse;
import io.helidon.webserver.http.RoutingResponse;

import java.io.OutputStream;

/**
 * HelidonHttpServerResponse
 */
class HelidonHttpServerResponse implements ScxHttpServerResponse {

    private final RoutingResponse response;
    private final HelidonHttpHeadersWritable headers;

    public HelidonHttpServerResponse(RoutingResponse response) {
        this.response = response;
        this.headers = new HelidonHttpHeadersWritable(response.headers());
    }

    @Override
    public HttpStatusCode status() {
        return HttpStatusCode.of(this.response.status().code());
    }

    @Override
    public ScxHttpHeadersWritable headers() {
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
    public boolean closed() {
        return this.response.isSent();
    }

}
