package cool.scx.http_server.helidon;

import cool.scx.http_server.HttpStatusCode;
import cool.scx.http_server.ScxHttpHeadersWritable;
import cool.scx.http_server.ScxHttpResponse;
import io.helidon.webserver.http.RoutingResponse;

public class HelidonHttpResponse implements ScxHttpResponse {

    private final RoutingResponse response;
    private final HelidonHttpHeadersWritable headers;

    public HelidonHttpResponse(RoutingResponse response) {
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
    public ScxHttpResponse status(HttpStatusCode code) {
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
