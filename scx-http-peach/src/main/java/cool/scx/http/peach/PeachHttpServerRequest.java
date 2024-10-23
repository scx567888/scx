package cool.scx.http.peach;

import cool.scx.http.*;
import cool.scx.http.uri.ScxURI;

/**
 * PeachHttpServerRequest
 */
class PeachHttpServerRequest implements ScxHttpServerRequest {

    ScxHttpMethod method;
    ScxURI uri;
    HttpVersion version;
    ScxHttpHeaders headers;
    ScxHttpBody body;
    ScxHttpServerResponse response;

    public PeachHttpServerRequest() {

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
        return null;
    }

    @Override
    public PeerInfo localPeer() {
        return null;
    }

}
