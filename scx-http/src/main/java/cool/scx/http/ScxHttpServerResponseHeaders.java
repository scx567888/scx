package cool.scx.http;

import cool.scx.http.content_type.ContentType;
import cool.scx.http.cookie.Cookie;

import static cool.scx.http.HttpFieldName.CONTENT_TYPE;

public interface ScxHttpServerResponseHeaders extends ScxHttpHeadersWritable {

    default ScxHttpServerResponseHeaders contentType(ContentType contentType) {
        set(CONTENT_TYPE, contentType.toString());
        return this;
    }

    default ScxHttpServerResponseHeaders contentType(MediaType mediaType) {
        return contentType(ContentType.of(mediaType));
    }

    ScxHttpServerResponseHeaders addCookie(Cookie cookie);

    ScxHttpServerResponseHeaders removeCookie(String name);

}
