package cool.scx.http.helidon;

import cool.scx.http.*;
import cool.scx.http.uri.URI;
import io.helidon.webserver.ConnectionContext;
import io.helidon.webserver.http.RoutingRequest;
import io.helidon.webserver.http.RoutingResponse;

class HelidonHttpServerRequest implements ScxHttpServerRequest {

    private final ScxHttpMethod method;
    private final URI uri;
    private final HttpVersion version;
    private final ScxHttpHeaders headers;
    private final ScxHttpBody body;
    private final ScxHttpServerResponse response;
    private final HelidonPeerInfo remotePeer;
    private final HelidonPeerInfo localPeer;

    public HelidonHttpServerRequest(ConnectionContext ctx, RoutingRequest request, RoutingResponse response) {
        var p = request.prologue();
        this.method = ScxHttpMethod.of(p.method().text());
        this.uri = URI.of().path(p.uriPath().path()).query(new HelidonURIQuery(p.query())).fragment(p.fragment().hasValue() ? p.fragment().value() : null);
        this.version = HttpVersion.of(p.rawProtocol());
        this.headers = new HelidonHttpHeaders<>(request.headers());
        this.body = new HelidonHttpBody(request.content());
        this.response = new HelidonHttpServerResponse(response);
        this.remotePeer = new HelidonPeerInfo(request.remotePeer());
        this.localPeer = new HelidonPeerInfo(request.localPeer());
    }

    @Override
    public ScxHttpMethod method() {
        return method;
    }

    @Override
    public URI uri() {
        return uri;
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

    @Override
    public PeerInfo remotePeer() {
        return remotePeer;
    }

    @Override
    public PeerInfo localPeer() {
        return localPeer;
    }

}
