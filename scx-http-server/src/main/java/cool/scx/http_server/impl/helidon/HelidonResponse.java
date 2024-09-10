package cool.scx.http_server.impl.helidon;

import cool.scx.http_server.ScxHttpResponse;
import io.helidon.webserver.http.RoutingResponse;

import java.io.File;

public class HelidonResponse implements ScxHttpResponse {

    private final RoutingResponse response;

    public HelidonResponse(RoutingResponse response) {
        this.response=response;
    }

    @Override
    public void write(byte[] bytes) {
        
    }

    @Override
    public void write(File file) {

    }

    @Override
    public void end(byte[] bytes) {

    }

    @Override
    public void end(File file) {

    }

    @Override
    public void end() {

    }
}
