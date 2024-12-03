package cool.scx.http.helidon;

import cool.scx.http.HttpStatusCode;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxHttpServerResponse;
import io.helidon.webserver.http.RoutingResponse;

import java.io.OutputStream;

/**
 * HelidonHttpServerResponse
 *
 * @author scx567888
 * @version 0.0.1
 */
class HelidonHttpServerResponse implements ScxHttpServerResponse {

    private final RoutingResponse response;
    private final ScxHttpHeadersWritable headers;
    private final HelidonHttpServerRequest request;

    public HelidonHttpServerResponse(HelidonHttpServerRequest request, RoutingResponse response) {
        this.request = request;
        this.response = response;
        this.headers = new HelidonHeadersWritable(response.headers());
    }

    @Override
    public ScxHttpServerRequest request() {
        return request;
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
    public void end() {
        this.response.commit();
    }

    @Override
    public boolean isClosed() {
        return this.response.isSent();
    }

}
