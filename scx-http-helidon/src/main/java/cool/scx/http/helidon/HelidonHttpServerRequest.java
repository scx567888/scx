package cool.scx.http.helidon;

import cool.scx.http.*;
import cool.scx.http.uri.ScxURI;
import io.helidon.webserver.ConnectionContext;
import io.helidon.webserver.http.RoutingRequest;
import io.helidon.webserver.http.RoutingResponse;

import static cool.scx.http.helidon.HelidonHelper.convertHeaders;
import static cool.scx.http.helidon.HelidonHelper.createScxURI;

/**
 * HelidonHttpServerRequest
 */
class HelidonHttpServerRequest implements ScxHttpServerRequest {

    private final ScxHttpMethod method;
    private final ScxURI uri;
    private final HttpVersion version;
    private final ScxHttpHeaders headers;
    private final ScxHttpBody body;
    private final ScxHttpServerResponse response;
    private final HelidonPeerInfo remotePeer;
    private final HelidonPeerInfo localPeer;

    public HelidonHttpServerRequest(ConnectionContext ctx, RoutingRequest request, RoutingResponse response) {
        var p = request.prologue();
        this.method = ScxHttpMethod.of(p.method().text());
        this.uri = createScxURI(p);
        this.version = HttpVersion.of(p.rawProtocol());
        this.headers = convertHeaders(request.headers());
        this.body = new ScxHttpBodyImpl(request.content().inputStream(), this.headers);
        this.response = new HelidonHttpServerResponse(this, response);
        this.remotePeer = new HelidonPeerInfo(request.remotePeer());
        this.localPeer = new HelidonPeerInfo(request.localPeer());
    }

    @Override
    public ScxHttpMethod method() {
        return method;
    }

    @Override
    public ScxURI uri() {
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
