package cool.scx.http;

import cool.scx.http.content_type.ContentType;
import cool.scx.http.cookie.Cookie;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media.byte_array.ByteArrayWriter;
import cool.scx.http.media.empty.EmptyWriter;
import cool.scx.http.media.input_stream.InputStreamWriter;
import cool.scx.http.media.path.PathWriter;
import cool.scx.http.media.string.StringWriter;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * ScxHttpServerResponse
 */
public interface ScxHttpServerResponse {

    ScxHttpServerRequest request();

    HttpStatusCode status();

    ScxHttpHeadersWritable headers();

    ScxHttpServerResponse status(HttpStatusCode code);

    OutputStream outputStream();

    void end();

    boolean isClosed();

    default void send(MediaWriter writer) {
        writer.beforeWrite(headers(), request().headers());
        writer.write(outputStream());
        end();
    }

    default void send() {
        send(new EmptyWriter());
    }

    default void send(byte[] bytes) {
        send(new ByteArrayWriter(bytes));
    }

    default void send(String str) {
        send(new StringWriter(str));
    }

    default void send(String str, Charset charset) {
        send(new StringWriter(str, charset));
    }

    default void send(Path path) {
        send(new PathWriter(path));
    }

    default void send(Path path, long offset, long length) {
        send(new PathWriter(path, offset, length));
    }

    default void send(InputStream inputStream) {
        send(new InputStreamWriter(inputStream));
    }

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

    default ContentType contentType() {
        return headers().contentType();
    }

    default ScxHttpServerResponse status(int code) {
        return status(HttpStatusCode.of(code));
    }

    default ScxHttpServerResponse contentType(ContentType contentType) {
        headers().contentType(contentType);
        return this;
    }

    default ScxHttpServerResponse addSetCookie(Cookie cookie) {
        headers().addSetCookie(cookie);
        return this;
    }

    default ScxHttpServerResponse removeSetCookie(String name) {
        headers().removeSetCookie(name);
        return this;
    }

    default ScxHttpServerResponse contentLength(long contentLength) {
        headers().contentLength(contentLength);
        return this;
    }

}
