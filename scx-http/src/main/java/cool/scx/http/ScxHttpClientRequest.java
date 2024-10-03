package cool.scx.http;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.cookie.Cookie;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media.byte_array.ByteArrayWriter;
import cool.scx.http.media.empty.EmptyWriter;
import cool.scx.http.media.input_stream.InputStreamWriter;
import cool.scx.http.media.json_node.JsonNodeWriter;
import cool.scx.http.media.multi_part.MultiPart;
import cool.scx.http.media.multi_part.MultiPartWriter;
import cool.scx.http.media.object.ObjectWriter;
import cool.scx.http.media.path.PathWriter;
import cool.scx.http.media.string.StringWriter;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * ScxHttpClientRequest
 */
public interface ScxHttpClientRequest {

    // ************* 基本方法 ****************
    ScxHttpMethod method();

    ScxURIWritable uri();

    ScxHttpHeadersWritable headers();

    ScxHttpClientRequest method(HttpMethod method);

    ScxHttpClientRequest uri(ScxURIWritable uri);

    ScxHttpClientRequest headers(ScxHttpHeadersWritable headers);

    ScxHttpClientResponse send(MediaWriter writer);

    //************** 简化操作 ***************

    default ScxHttpClientRequest uri(String uri) {
        return uri(ScxURI.of(uri));
    }

    default ScxHttpClientResponse send() {
        return send(new EmptyWriter());
    }

    default ScxHttpClientResponse send(byte[] bytes) {
        return send(new ByteArrayWriter(bytes));
    }

    default ScxHttpClientResponse send(String str) {
        return send(new StringWriter(str));
    }

    default ScxHttpClientResponse send(String str, Charset charset) {
        return send(new StringWriter(str, charset));
    }

    default ScxHttpClientResponse send(Path path) {
        return send(new PathWriter(path));
    }

    default ScxHttpClientResponse send(Path path, long offset, long length) {
        return send(new PathWriter(path, offset, length));
    }

    default ScxHttpClientResponse send(InputStream inputStream) {
        return send(new InputStreamWriter(inputStream));
    }

    default ScxHttpClientResponse send(MultiPart multiPart) {
        return send(new MultiPartWriter(multiPart));
    }

    default ScxHttpClientResponse send(JsonNode jsonNode) {
        return send(new JsonNodeWriter(jsonNode));
    }

    default ScxHttpClientResponse send(Object object) {
        return send(new ObjectWriter(object));
    }

    //************** 简化 Headers 操作 *************

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

    default ContentType contentType() {
        return headers().contentType();
    }

    default ScxHttpClientRequest contentType(ContentType contentType) {
        headers().contentType(contentType);
        return this;
    }

    default Long contentLength() {
        return headers().contentLength();
    }

    default ScxHttpClientRequest contentLength(long contentLength) {
        headers().contentLength(contentLength);
        return this;
    }

}
