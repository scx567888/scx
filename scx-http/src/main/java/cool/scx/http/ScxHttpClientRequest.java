package cool.scx.http;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.http.headers.ScxHttpHeaderName;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.headers.content_encoding.ScxContentEncoding;
import cool.scx.http.headers.cookie.Cookie;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media.byte_array.ByteArrayWriter;
import cool.scx.http.media.form_params.FormParams;
import cool.scx.http.media.form_params.FormParamsWriter;
import cool.scx.http.media.gzip.ClientGzipSender;
import cool.scx.http.media.input_stream.InputStreamWriter;
import cool.scx.http.media.json_node.JsonNodeWriter;
import cool.scx.http.media.multi_part.MultiPart;
import cool.scx.http.media.multi_part.MultiPartWriter;
import cool.scx.http.media.object.ObjectWriter;
import cool.scx.http.media.path.PathWriter;
import cool.scx.http.media.string.StringWriter;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.method.HttpMethod;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.http.version.HttpVersion;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;

import static cool.scx.http.media.empty.EmptyWriter.EMPTY_WRITER;

/// ScxHttpClientRequest
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpClientRequest {

    HttpVersion version();

    ScxHttpMethod method();

    ScxURIWritable uri();

    ScxHttpHeadersWritable headers();

    ScxHttpClientRequest version(HttpVersion version);

    ScxHttpClientRequest method(HttpMethod method);

    ScxHttpClientRequest uri(ScxURI uri);

    ScxHttpClientRequest headers(ScxHttpHeaders headers);

    ScxHttpClientResponse send(MediaWriter writer);

    //******************** send 操作 *******************

    default ScxHttpClientResponse send() {
        return send(EMPTY_WRITER);
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

    default ScxHttpClientResponse send(FormParams formParams) {
        return send(new FormParamsWriter(formParams));
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

    default ClientGzipSender sendGzip() {
        return new ClientGzipSender(this);
    }

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
