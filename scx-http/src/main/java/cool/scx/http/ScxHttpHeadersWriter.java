package cool.scx.http;

import cool.scx.http.headers.ScxHttpHeaderName;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.headers.content_encoding.ScxContentEncoding;
import cool.scx.http.headers.cookie.Cookie;
import cool.scx.http.media_type.ScxMediaType;

/// 这只是一个帮助类 用于简化 header 的写入
public interface ScxHttpHeadersWriter<T extends ScxHttpHeadersWriter<T>> {

    ScxHttpHeadersWritable headers();

    default T setHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().set(headerName, values);
        return (T) this;
    }

    default T addHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().add(headerName, values);
        return (T) this;
    }

    default T setHeader(String headerName, String... values) {
        this.headers().set(headerName, values);
        return (T) this;
    }

    default T addHeader(String headerName, String... values) {
        this.headers().add(headerName, values);
        return (T) this;
    }

    default T addSetCookie(Cookie... cookie) {
        headers().addSetCookie(cookie);
        return (T) this;
    }

    default T removeSetCookie(String name) {
        headers().removeSetCookie(name);
        return (T) this;
    }

    default ScxMediaType contentType() {
        return headers().contentType();
    }

    default T contentType(ScxMediaType contentType) {
        headers().contentType(contentType);
        return (T) this;
    }

    default Long contentLength() {
        return headers().contentLength();
    }

    default T contentLength(long contentLength) {
        headers().contentLength(contentLength);
        return (T) this;
    }

    default ScxContentEncoding contentEncoding() {
        return headers().contentEncoding();
    }

    default T contentEncoding(ScxContentEncoding contentEncoding) {
        headers().contentEncoding(contentEncoding);
        return (T) this;
    }
    
}
