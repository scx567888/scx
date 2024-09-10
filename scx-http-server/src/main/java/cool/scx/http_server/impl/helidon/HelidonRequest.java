package cool.scx.http_server.impl.helidon;

import cool.scx.http_server.*;
import io.helidon.webserver.ConnectionContext;
import io.helidon.webserver.http.RoutingRequest;
import io.helidon.webserver.http.RoutingResponse;

import java.io.IOException;
import java.io.InputStream;

import static cool.scx.http_server.Helper.*;

public class HelidonRequest implements ScxHttpRequest {

    private final ConnectionContext ctx;
    private final RoutingRequest request;
    private final ScxHttpMethod method;
    private final ScxHttpPath path;
    private final ScxHttpVersion version;
    private final ScxHttpHeaders headers;
    private final ScxHttpBody body;
    private final ScxHttpResponse response;

    public HelidonRequest(ConnectionContext ctx, RoutingRequest request, RoutingResponse response) {
        this.ctx = ctx;
        this.request = request;
        this.method = createScxHttpMethod(request.prologue().method());
        this.path = createScxHttpPath(request.prologue());
        this.version = createScxHttpVersion(request.prologue().rawProtocol());
        this.headers = createScxHttpHeaders(request.headers());
        this.body = new HelidonHttpBody(request.content());
        this.response = new HelidonResponse(response);
    }

    @Override
    public ScxHttpMethod method() {
        return method;
    }

    @Override
    public ScxHttpPath path() {
        return path;
    }

    @Override
    public ScxHttpVersion version() {
        return version;
    }

    @Override
    public ScxHttpHeaders headers() {
        return headers;
    }

    @Override
    public ScxHttpBody body() {
        return body;
    }

    @Override
    public ScxHttpResponse response() {
        return this.response;
    }

}
