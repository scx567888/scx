package cool.scx.http;

import cool.scx.http.content_type.ContentType;
import cool.scx.http.cookie.Cookie;
import cool.scx.http.cookie.Cookies;
import cool.scx.http.uri.ScxURI;

/**
 * ScxHttpServerRequest
 */
public interface ScxHttpServerRequest {

    ScxHttpMethod method();

    ScxURI uri();

    HttpVersion version();

    ScxHttpHeaders headers();

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

    default Cookies cookies() {
        return headers().cookies();
    }

    default Cookie getCookie(String name) {
        return headers().getCookie(name);
    }

}
