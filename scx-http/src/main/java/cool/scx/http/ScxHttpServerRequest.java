package cool.scx.http;

import cool.scx.http.content_type.ContentType;
import cool.scx.http.cookie.Cookie;
import cool.scx.http.cookie.Cookies;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.URIPath;
import cool.scx.http.uri.URIQuery;

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

    Cookies cookies();

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

    default ContentType contentType() {
        return ContentType.of(getHeader(HttpFieldName.CONTENT_TYPE));
    }

    default Cookie getCookie(String name) {
        return cookies().get(name);
    }

}
