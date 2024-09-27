package cool.scx.http.helidon;

import cool.scx.http.HttpStatusCode;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.ScxHttpServerResponse;
import io.helidon.webserver.http.RoutingResponse;

import java.io.OutputStream;

import static cool.scx.http.helidon.HelidonHelper.convertHeaders;
import static cool.scx.http.helidon.HelidonHelper.updateHeaders;

/**
 * HelidonHttpServerResponse
 */
class HelidonHttpServerResponse implements ScxHttpServerResponse {

    private final RoutingResponse response;
    private final ScxHttpHeadersWritable headers;

    public HelidonHttpServerResponse(RoutingResponse response) {
        this.response = response;
        this.headers = convertHeaders(response.headers());
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
        updateHeaders(this.headers, this.response.headers());
        this.response.send();
    }

    @Override
    public void send(byte[] data) {
        updateHeaders(this.headers, this.response.headers());
        this.response.send(data);
    }

    @Override
    public void send(String data) {
        updateHeaders(this.headers, this.response.headers());
        this.response.send(data);
    }

    @Override
    public void send(Object data) {
        updateHeaders(this.headers, this.response.headers());
        this.response.send(data);
    }

    @Override
    public void end() {
        updateHeaders(this.headers, this.response.headers());
        this.response.commit();
    }

    @Override
    public boolean isClosed() {
        return this.response.isSent();
    }

}
