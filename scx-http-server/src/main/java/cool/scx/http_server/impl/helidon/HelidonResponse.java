package cool.scx.http_server.impl.helidon;

import cool.scx.http_server.ScxHttpResponse;
import io.helidon.webserver.http.RoutingResponse;
import io.vertx.core.Future;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HelidonResponse implements ScxHttpResponse {

    private final RoutingResponse response;

    public HelidonResponse(RoutingResponse response) {
        this.response = response;
    }

    @Override
    public void write(byte[] bytes) {

    }

    @Override
    public void write(File file) {

    }

    @Override
    public Future<Void> sendFile(File filename, long offset, long length) {
        return null;
    }

    @Override
    public void end(byte[] bytes) {
        response.send(bytes);
    }

    @Override
    public void end(File file) {
        try (var i = new FileInputStream(file)) {
            i.transferTo(response.outputStream());
//            end();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void end() {
        response.send();
    }
}
