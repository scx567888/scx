package cool.scx.http;

import cool.scx.http.headers.ScxHttpHeaderName;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.headers.content_encoding.ScxContentEncoding;
import cool.scx.http.headers.cookie.Cookie;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.status.ScxHttpStatus;

/// ScxHttpServerResponse
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpServerResponse extends ScxHttpSender<Void> {

    ScxHttpServerRequest request();

    ScxHttpStatus status();

    ScxHttpHeadersWritable headers();

    ScxHttpServerResponse status(ScxHttpStatus code);

    boolean isSent();

    //******************** 简化操作 *******************

    default ScxHttpServerResponse status(int code) {
        return status(ScxHttpStatus.of(code));
    }

    //******************** 简化 Headers 操作 *******************

    default ScxHttpServerResponse setHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().set(headerName, values);
        return this;
    }

    default ScxHttpServerResponse addHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().add(headerName, values);
        return this;
    }

    default ScxHttpServerResponse setHeader(String headerName, String... values) {
        this.headers().set(headerName, values);
        return this;
    }

    default ScxHttpServerResponse addHeader(String headerName, String... values) {
        this.headers().add(headerName, values);
        return this;
    }

    default ScxHttpServerResponse addSetCookie(Cookie... cookie) {
        headers().addSetCookie(cookie);
        return this;
    }

    default ScxHttpServerResponse removeSetCookie(String name) {
        headers().removeSetCookie(name);
        return this;
    }

    default ScxMediaType contentType() {
        return headers().contentType();
    }

    default ScxHttpServerResponse contentType(ScxMediaType contentType) {
        headers().contentType(contentType);
        return this;
    }

    default Long contentLength() {
        return headers().contentLength();
    }

    default ScxHttpServerResponse contentLength(long contentLength) {
        headers().contentLength(contentLength);
        return this;
    }

    default ScxContentEncoding contentEncoding() {
        return headers().contentEncoding();
    }

    default ScxHttpServerResponse contentEncoding(ScxContentEncoding contentEncoding) {
        headers().contentEncoding(contentEncoding);
        return this;
    }

}
