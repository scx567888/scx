package cool.scx.http.helidon;

import cool.scx.http.*;
import io.helidon.webserver.ConnectionContext;
import io.helidon.webserver.http.RoutingRequest;
import io.helidon.webserver.http.RoutingResponse;

import static cool.scx.http.helidon.HelidonHelper.createScxHttpPath;

public class HelidonHttpServerRequest implements ScxHttpServerRequest {

    private final ScxHttpMethod method;
    private final ScxHttpPath path;
    private final HttpVersion version;
    private final ScxHttpHeaders headers;
    private final ScxHttpBody body;
    private final ScxHttpServerResponse response;

    public HelidonHttpServerRequest(ConnectionContext ctx, RoutingRequest request, RoutingResponse response) {
        this.method = ScxHttpMethod.of(request.prologue().method().text());
        this.path = createScxHttpPath(request.prologue().uriPath(), request.query());
        this.version = HttpVersion.of(request.prologue().rawProtocol());
        this.headers = new HelidonHttpHeaders<>(request.headers());
        this.body = new HelidonHttpBody(request.content());
        this.response = new HelidonHttpServerResponse(response);
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
    public HttpVersion version() {
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
    public ScxHttpServerResponse response() {
        return this.response;
    }

}
