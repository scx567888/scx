package cool.scx.http.headers;

import cool.scx.http.headers.accept.Accept;
import cool.scx.http.headers.connection.Connection;
import cool.scx.http.headers.content_disposition.ContentDisposition;
import cool.scx.http.headers.cookie.Cookie;
import cool.scx.http.headers.cookie.Cookies;
import cool.scx.http.headers.transfer_encoding.TransferEncoding;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.parameters.Parameters;

import java.util.List;

import static cool.scx.http.headers.HttpFieldName.*;
import static cool.scx.http.headers.ScxHttpHeadersHelper.parseHeaders;

/// 只读的 Headers 可用于 ServerRequest 和 ClientResponse
/// 在 Parameters 的基础上实现了一些 方便操作 Http 头协议的方法
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpHeaders extends Parameters<ScxHttpHeaderName, String> {

    static ScxHttpHeadersWritable of() {
        return new ScxHttpHeadersImpl();
    }

    static ScxHttpHeadersWritable of(String headerStr) {
        return parseHeaders(headerStr);
    }

    static ScxHttpHeadersWritable of(ScxHttpHeaders oldHeaders) {
        return new ScxHttpHeadersImpl(oldHeaders);
    }

    default String get(String name) {
        return get(ScxHttpHeaderName.of(name));
    }

    default List<String> getAll(String name) {
        return getAll(ScxHttpHeaderName.of(name));
    }

    default boolean contains(String name) {
        return contains(ScxHttpHeaderName.of(name));
    }

    default Cookies cookies() {
        var c = get(COOKIE);
        return c != null ? Cookies.of(c) : null;
    }

    default Cookies setCookies() {
        //todo 这里没有判空
        return Cookies.of(getAll(SET_COOKIE).toArray(String[]::new));
    }

    default ScxMediaType contentType() {
        var v = get(CONTENT_TYPE);
        return v != null ? ScxMediaType.of(v) : null;
    }

    default ContentDisposition contentDisposition() {
        var c = get(CONTENT_DISPOSITION);
        return c != null ? ContentDisposition.of(c) : null;
    }

    default Long contentLength() {
        var c = get(CONTENT_LENGTH);
        return c != null ? Long.parseLong(c) : null;
    }

    default Cookie getCookie(String name) {
        var v = cookies();
        return v != null ? v.get(name) : null;
    }

    default Cookie getSetCookie(String name) {
        var v = setCookies();
        return v != null ? v.get(name) : null;
    }

    default Accept accept() {
        var c = get(ACCEPT);
        return c != null ? Accept.of(c) : null;
    }

    default TransferEncoding transferEncoding() {
        var c = get(TRANSFER_ENCODING);
        return c != null ? TransferEncoding.parseTransferEncoding(c) : null;
    }

    default Connection connection() {
        var c = get(CONNECTION);
        return c != null ? Connection.parseConnection(c) : null;
    }

    default String encode() {
        return ScxHttpHeadersHelper.encodeHeaders(this);
    }

}
