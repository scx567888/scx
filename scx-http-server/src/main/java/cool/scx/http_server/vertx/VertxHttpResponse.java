package cool.scx.http_server.vertx;

import cool.scx.http_server.ScxHttpResponse;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;

import java.io.File;

public class VertxHttpResponse implements ScxHttpResponse {

    private final HttpServerResponse httpServerResponse;

    public VertxHttpResponse(HttpServerResponse httpServerResponse) {
        this.httpServerResponse = httpServerResponse;
    }

    @Override
    public void write(byte[] bytes) {

    }

    @Override
    public void write(File file) {

    }

    @Override
    public void end(byte[] bytes) {
        this.httpServerResponse.end(Buffer.buffer(bytes));
    }

    @Override
    public void end(File file) {

    }

    @Override
    public void end() {

    }
    
}
