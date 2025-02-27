package cool.scx.http;

import cool.scx.http.content_type.ContentType;
import cool.scx.http.cookie.Cookie;
import cool.scx.http.cookie.Cookies;

/// ScxHttpClientResponse
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpClientResponse {

    HttpStatusCode status();

    ScxHttpHeaders headers();

    ScxHttpBody body();

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
