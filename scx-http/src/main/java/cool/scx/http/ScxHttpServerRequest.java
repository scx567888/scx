package cool.scx.http;

import cool.scx.http.content_type.ContentType;
import cool.scx.http.uri.ScxURI;

/**
 * ScxHttpServerRequest
 */
public interface ScxHttpServerRequest {

    ScxHttpMethod method();

    ScxURI uri();

    HttpVersion version();

    ScxHttpServerRequestHeaders headers();

    ScxHttpBody body();

    ScxHttpServerResponse response();

    PeerInfo remotePeer();

    PeerInfo localPeer();

    default String path() {
        return uri().path();
    }

    default Parameters<String, String> query() {
        return uri().query();
    }

    default String getHeader(ScxHttpHeaderName name) {
        return headers().get(name);
    }

    default String getHeader(String name) {
        return headers().get(name);
    }

    default ContentType contentType() {
        return headers().contentType();
    }

}
