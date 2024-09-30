package cool.scx.http;

import cool.scx.http.content_disposition.ContentDisposition;
import cool.scx.http.content_disposition.ContentDispositionWritable;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.content_type.ContentTypeWritable;
import cool.scx.http.cookie.Cookie;
import cool.scx.http.cookie.Cookies;
import cool.scx.http.cookie.CookiesWritable;

import static cool.scx.http.HttpFieldName.*;

/**
 * 可写的 Headers 可用于 ServerResponse 和 ClientRequest
 * 在 Parameters 的基础上实现了一些 方便操作 Http 头协议的方法
 */
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

    default CookiesWritable cookies() {
        return Cookies.of(get(COOKIE));
    }

    default ScxHttpHeadersWritable cookies(Cookies cookies) {
        set(COOKIE, cookies.encodeCookie());
        return this;
    }

    default CookiesWritable setCookies() {
        return Cookies.of(getAll(SET_COOKIE).toArray(String[]::new));
    }

    default ScxHttpHeadersWritable setCookies(Cookies cookies) {
        set(SET_COOKIE, cookies.encodeSetCookie());
        return this;
    }

    default ContentTypeWritable contentType() {
        return ContentType.of(get(CONTENT_TYPE));
    }

    default ScxHttpHeadersWritable contentType(ContentType contentType) {
        set(CONTENT_TYPE, contentType.encode());
        return this;
    }

    default ContentDispositionWritable contentDisposition() {
        return ContentDisposition.of(get(CONTENT_DISPOSITION));
    }

    default ScxHttpHeadersWritable contentDisposition(ContentDisposition contentDisposition) {
        set(CONTENT_DISPOSITION, contentDisposition.encode());
        return this;
    }

    default ScxHttpHeadersWritable addCookie(Cookie... cookie) {
        var cookies = cookies();
        for (var c : cookie) {
            cookies.add(c);
        }
        return cookies(cookies);
    }

    default ScxHttpHeadersWritable removeCookie(String name) {
        var cookies = cookies();
        cookies.remove(name);
        return cookies(cookies);
    }

    default ScxHttpHeadersWritable addSetCookie(Cookie... cookie) {
        var cookies = setCookies();
        for (var c : cookie) {
            cookies.add(c);
        }
        return setCookies(cookies);
    }

    default ScxHttpHeadersWritable removeSetCookie(String name) {
        var cookies = setCookies();
        cookies.remove(name);
        return setCookies(cookies);
    }

    default ScxHttpHeadersWritable contentLength(long contentLength) {
        set(CONTENT_LENGTH, String.valueOf(contentLength));
        return this;
    }

}
