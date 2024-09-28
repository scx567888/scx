package cool.scx.http;

import cool.scx.http.media.MediaWriter;
import cool.scx.http.media.byte_array.ByteArrayWriter;
import cool.scx.http.media.input_stream.InputStreamWriter;
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

    ScxHttpMethod method();

    ScxURIWritable uri();

    ScxHttpHeadersWritable headers();

    ScxHttpClientRequest method(HttpMethod method);

    ScxHttpClientRequest uri(ScxURIWritable uri);

    ScxHttpClientRequest headers(ScxHttpHeadersWritable headers);

    ScxHttpClientResponse send(MediaWriter writer);

    default ScxHttpClientResponse send() {
        return send(new byte[]{});
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

    default ScxHttpClientRequest uri(String uri) {
        return uri(ScxURI.of(uri));
    }

    default ScxHttpClientRequest setHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().set(headerName, values);
        return this;
    }

}
