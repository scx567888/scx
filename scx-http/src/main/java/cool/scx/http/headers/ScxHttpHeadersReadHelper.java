package cool.scx.http.headers;

import cool.scx.http.headers.content_encoding.ScxContentEncoding;
import cool.scx.http.headers.cookie.Cookie;
import cool.scx.http.headers.cookie.Cookies;
import cool.scx.http.media_type.ScxMediaType;

/// 这只是一个帮助类 用于简化 header 的读取
public interface ScxHttpHeadersReadHelper {

    ScxHttpHeaders headers();

    default String getHeader(ScxHttpHeaderName name) {
        return headers().get(name);
    }

    default String getHeader(String name) {
        return headers().get(name);
    }

    default ScxMediaType contentType() {
        return headers().contentType();
    }

    default Long contentLength() {
        return headers().contentLength();
    }

    default ScxContentEncoding contentEncoding() {
        return headers().contentEncoding();
    }

    default Cookies cookies() {
        return headers().cookies();
    }

    default Cookie getCookie(String name) {
        return headers().getCookie(name);
    }

    default Cookies setCookies() {
        return headers().setCookies();
    }

    default Cookie getSetCookie(String name) {
        return headers().getSetCookie(name);
    }

}
