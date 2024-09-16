package cool.scx.http;

import cool.scx.http.uri.URI;
import cool.scx.http.uri.URIPath;
import cool.scx.http.uri.URIQuery;

/**
 * ScxHttpServerRequest
 */
public interface ScxHttpServerRequest {

    ScxHttpMethod method();

    URI uri();

    HttpVersion version();

    ScxHttpHeaders headers();

    ScxHttpBody body();

    ScxHttpServerResponse response();

    PeerInfo remotePeer();

    PeerInfo localPeer();

    default URIPath path() {
        return uri().path();
    }

    default URIQuery query() {
        return uri().query();
    }

    default String getHeader(ScxHttpHeaderName name) {
        return headers().get(name);
    }

    default String getHeader(String name) {
        return headers().get(name);
    }

}
