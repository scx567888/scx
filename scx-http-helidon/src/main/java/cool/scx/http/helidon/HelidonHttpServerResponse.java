package cool.scx.http.helidon;

import cool.scx.http.HttpStatusCode;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.ScxHttpServerResponse;
import io.helidon.webserver.http.RoutingResponse;

class HelidonHttpServerResponse implements ScxHttpServerResponse {

    private final RoutingResponse response;
    private final HelidonHttpHeadersWritable headers;

    public HelidonHttpServerResponse(RoutingResponse response) {
        this.response = response;
        this.headers = new HelidonHttpHeadersWritable(response.headers());
    }

    @Override
    public HttpStatusCode statusCode() {
        return HttpStatusCode.of(this.response.status().code());
    }

    @Override
    public ScxHttpHeadersWritable headers() {
        return headers;
    }

    @Override
    public ScxHttpServerResponse setStatusCode(HttpStatusCode code) {
        this.response.status(code.code());
        return this;
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
    public void send() {
        this.response.send();
    }

    @Override
    public boolean closed() {
        return this.response.isSent();
    }

}
