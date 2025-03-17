package cool.scx.http;

import cool.scx.http.headers.ScxHttpHeaderName;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.cookie.Cookie;
import cool.scx.http.headers.cookie.Cookies;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.status.HttpStatusCode;

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

    default ScxMediaType contentType() {
        return headers().contentType();
    }

    default Cookies cookies() {
        return headers().cookies();
    }

    default Cookie getCookie(String name) {
        return headers().getCookie(name);
    }

}
