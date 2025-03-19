package cool.scx.http.headers;

import cool.scx.http.headers.content_disposition.ContentDisposition;
import cool.scx.http.headers.cookie.Cookie;
import cool.scx.http.headers.cookie.Cookies;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.parameters.ParametersWritable;

import static cool.scx.http.headers.HttpFieldName.*;

/// 可写的 Headers 可用于 ServerResponse 和 ClientRequest
/// 在 Parameters 的基础上实现了一些 方便操作 Http 头协议的方法
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpHeadersWritable extends ScxHttpHeaders, ParametersWritable<ScxHttpHeaderName, String> {

    default ScxHttpHeadersWritable set(String name, String... value) {
        set(ScxHttpHeaderName.of(name), value);
        return this;
    }

    default ScxHttpHeadersWritable add(String name, String... value) {
        add(ScxHttpHeaderName.of(name), value);
        return this;
    }

    default ScxHttpHeadersWritable remove(String name) {
        remove(ScxHttpHeaderName.of(name));
        return this;
    }

    default ScxHttpHeadersWritable cookies(Cookies cookies) {
        set(COOKIE, cookies.encodeCookie());
        return this;
    }

    default ScxHttpHeadersWritable setCookies(Cookies cookies) {
        set(SET_COOKIE, cookies.encodeSetCookie());
        return this;
    }

    default ScxHttpHeadersWritable contentType(ScxMediaType mediaType) {
        set(CONTENT_TYPE, mediaType.encode());
        return this;
    }

    default ScxHttpHeadersWritable contentDisposition(ContentDisposition contentDisposition) {
        set(CONTENT_DISPOSITION, contentDisposition.encode());
        return this;
    }

    default ScxHttpHeadersWritable addCookie(Cookie... cookie) {
        var oldCookies = cookies();
        var newCookies = oldCookies != null ? Cookies.of(oldCookies) : Cookies.of();
        for (var c : cookie) {
            newCookies.add(c);
        }
        return cookies(newCookies);
    }

    default ScxHttpHeadersWritable removeCookie(String name) {
        var oldCookies = cookies();
        if (oldCookies == null) {
            return this;
        }
        var newCookies = Cookies.of(oldCookies);
        newCookies.remove(name);
        return cookies(newCookies);
    }

    default ScxHttpHeadersWritable addSetCookie(Cookie... cookie) {
        var oldSetCookies = setCookies();
        var newSetCookies = oldSetCookies != null ? Cookies.of(oldSetCookies) : Cookies.of();
        for (var c : cookie) {
            newSetCookies.add(c);
        }
        return setCookies(newSetCookies);
    }

    default ScxHttpHeadersWritable removeSetCookie(String name) {
        var oldSetCookies = setCookies();
        if (oldSetCookies == null) {
            return this;
        }
        var newSetCookies = Cookies.of(oldSetCookies);
        newSetCookies.remove(name);
        return cookies(newSetCookies);
    }

    default ScxHttpHeadersWritable contentLength(long contentLength) {
        set(CONTENT_LENGTH, String.valueOf(contentLength));
        return this;
    }

}
