package cool.scx.http.helidon;

import cool.scx.http.*;
import cool.scx.http.uri.URIPath;
import cool.scx.http.uri.URIQuery;
import io.helidon.webserver.ConnectionContext;
import io.helidon.webserver.http.RoutingRequest;
import io.helidon.webserver.http.RoutingResponse;

class HelidonHttpServerRequest implements ScxHttpServerRequest {

    private final ScxHttpMethod method;
    private final URIPath path;
    private final URIQuery query;
    private final HttpVersion version;
    private final ScxHttpHeaders headers;
    private final ScxHttpBody body;
    private final ScxHttpServerResponse response;

    public HelidonHttpServerRequest(ConnectionContext ctx, RoutingRequest request, RoutingResponse response) {
        this.method = ScxHttpMethod.of(request.prologue().method().text());
        this.path = URIPath.of().value(request.prologue().uriPath().path());
        this.query = new HelidonURIQuery(request.prologue().query());
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
    public URIPath path() {
        return path;
    }

    @Override
    public URIQuery query() {
        return query;
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
