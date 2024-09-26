package cool.scx.http;

import cool.scx.http.content_type.ContentType;
import cool.scx.http.cookie.Cookie;
import cool.scx.http.cookie.Cookies;

public interface ScxHttpServerRequestHeaders extends ScxHttpHeaders {

    Cookies cookies();

    default ContentType contentType() {
        return ContentType.of(get(HttpFieldName.CONTENT_TYPE));
    }

    default Cookie getCookie(String name) {
        return cookies().get(name);
    }

}
