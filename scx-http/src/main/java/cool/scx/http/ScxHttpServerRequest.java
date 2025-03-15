package cool.scx.http;

import cool.scx.http.headers.ScxHttpHeaderName;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.content_type.ContentType;
import cool.scx.http.headers.cookie.Cookie;
import cool.scx.http.headers.cookie.Cookies;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.uri.ScxURI;

/// ScxHttpServerRequest
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpServerRequest {

    ScxHttpServerResponse response();

    ScxHttpMethod method();

    ScxURI uri();

    HttpVersion version();

    ScxHttpHeaders headers();

    ScxHttpBody body();

    PeerInfo remotePeer();

    PeerInfo localPeer();

    default String path() {
        return uri().path();
    }

    default Parameters<String, String> query() {
        return uri().query();
    }

    default String getQuery(String name) {
        return uri().getQuery(name);
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
