package cool.scx.web.http_server.vertx;

import cool.scx.web.http_server.ScxHttpRequest;
import cool.scx.web.http_server.ScxHttpResponse;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;

public class VertxHttpRequest implements ScxHttpRequest {

    private final HttpServerRequest httpServerRequest;

    public VertxHttpRequest(HttpServerRequest httpServerRequest) {
        this.httpServerRequest = httpServerRequest;
    }

    @Override
    public ScxHttpResponse response() {
        return new VertxHttpResponse(this.httpServerRequest.response());
    }

    @Override
    public byte[] body() {
        var bodyFuture = this.httpServerRequest.body();
        var buffer = VertxHelper.await(bodyFuture);
        return buffer.getBytes();
    }

    @Override
    public Future<Buffer> body1() {
        return this.httpServerRequest.body();
    }

}
