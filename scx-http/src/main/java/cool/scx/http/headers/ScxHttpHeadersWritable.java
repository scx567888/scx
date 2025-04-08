package cool.scx.http.headers;

import cool.scx.http.headers.content_disposition.ContentDisposition;
import cool.scx.http.headers.cookie.Cookie;
import cool.scx.http.headers.cookie.Cookies;
import cool.scx.http.headers.cookie.CookiesWritable;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.parameters.ParametersWritable;

import static cool.scx.http.headers.HttpFieldName.*;

/// 可写的 Headers 可用于 ServerResponse 和 ClientRequest
/// 在 Parameters 的基础上实现了一些 方便操作 Http 头协议的方法
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpHeadersWritable extends ScxHttpHeaders, ParametersWritable<ScxHttpHeaderName, String> {

    @Override
    ScxHttpHeadersWritable set(ScxHttpHeaderName name, String... value);

    @Override
    ScxHttpHeadersWritable add(ScxHttpHeaderName name, String... value);

    @Override
    ScxHttpHeadersWritable remove(ScxHttpHeaderName name);

    @Override
    ScxHttpHeadersWritable clear();

    default ScxHttpHeadersWritable set(String name, String... value) {
        return set(ScxHttpHeaderName.of(name), value);
    }

    default ScxHttpHeadersWritable add(String name, String... value) {
        return add(ScxHttpHeaderName.of(name), value);
    }

    default ScxHttpHeadersWritable remove(String name) {
        return remove(ScxHttpHeaderName.of(name));
    }

    default ScxHttpHeadersWritable cookies(Cookies cookies) {
        return set(COOKIE, cookies.encodeCookie());
    }

    default ScxHttpHeadersWritable setCookies(Cookies cookies) {
        return set(SET_COOKIE, cookies.encodeSetCookie());
    }

    default ScxHttpHeadersWritable contentType(ScxMediaType mediaType) {
        return set(CONTENT_TYPE, mediaType.encode());
    }

    default ScxHttpHeadersWritable contentDisposition(ContentDisposition contentDisposition) {
        return set(CONTENT_DISPOSITION, contentDisposition.encode());
    }

    default ScxHttpHeadersWritable addCookie(Cookie... cookie) {
        var newCookies = (CookiesWritable) cookies();
        for (var c : cookie) {
            newCookies.add(c);
        }
        return cookies(newCookies);
    }

    default ScxHttpHeadersWritable removeCookie(String name) {
        var newCookies = (CookiesWritable) cookies();
        newCookies.remove(name);
        return cookies(newCookies);
    }

    default ScxHttpHeadersWritable addSetCookie(Cookie... cookie) {
        var newSetCookies = (CookiesWritable) setCookies();
        for (var c : cookie) {
            newSetCookies.add(c);
        }
        return setCookies(newSetCookies);
    }

    default ScxHttpHeadersWritable removeSetCookie(String name) {
        var newSetCookies = (CookiesWritable) setCookies();
        newSetCookies.remove(name);
        return cookies(newSetCookies);
    }

    default ScxHttpHeadersWritable contentLength(long contentLength) {
        return set(CONTENT_LENGTH, String.valueOf(contentLength));
    }

}
