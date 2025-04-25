package cool.scx.http;

import cool.scx.http.headers.ScxHttpHeaderName;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.headers.content_encoding.ScxContentEncoding;
import cool.scx.http.headers.cookie.Cookie;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.method.HttpMethod;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.http.version.HttpVersion;

/// ScxHttpClientRequest
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpClientRequest extends ScxHttpSender<ScxHttpClientResponse> {

    HttpVersion version();

    ScxHttpMethod method();

    ScxURIWritable uri();

    ScxHttpHeadersWritable headers();

    ScxHttpClientRequest version(HttpVersion version);

    ScxHttpClientRequest method(HttpMethod method);

    ScxHttpClientRequest uri(ScxURI uri);

    ScxHttpClientRequest headers(ScxHttpHeaders headers);

    //******************** 简化操作 *******************

    default ScxHttpClientRequest uri(String uri) {
        return uri(ScxURI.of(uri));
    }

    //******************** 简化 Headers 操作 *******************

    default ScxHttpClientRequest setHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().set(headerName, values);
        return this;
    }

    default ScxHttpClientRequest addHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().add(headerName, values);
        return this;
    }

    default ScxHttpClientRequest setHeader(String headerName, String... values) {
        this.headers().set(headerName, values);
        return this;
    }

    default ScxHttpClientRequest addHeader(String headerName, String... values) {
        this.headers().add(headerName, values);
        return this;
    }

    default ScxHttpClientRequest addCookie(Cookie... cookie) {
        headers().addCookie(cookie);
        return this;
    }

    default ScxHttpClientRequest removeCookie(String name) {
        headers().removeCookie(name);
        return this;
    }

    default ScxMediaType contentType() {
        return headers().contentType();
    }

    default ScxHttpClientRequest contentType(ScxMediaType mediaType) {
        headers().contentType(mediaType);
        return this;
    }

    default Long contentLength() {
        return headers().contentLength();
    }

    default ScxHttpClientRequest contentLength(long contentLength) {
        headers().contentLength(contentLength);
        return this;
    }

    default ScxContentEncoding contentEncoding() {
        return headers().contentEncoding();
    }

    default ScxHttpClientRequest contentEncoding(ScxContentEncoding contentEncoding) {
        headers().contentEncoding(contentEncoding);
        return this;
    }

}
