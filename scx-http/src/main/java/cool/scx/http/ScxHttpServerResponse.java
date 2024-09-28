package cool.scx.http;

import cool.scx.http.content_type.ContentType;
import cool.scx.http.cookie.Cookie;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media.byte_array.ByteArrayWriter;
import cool.scx.http.media.string.StringWriter;

import java.io.OutputStream;

/**
 * ScxHttpServerResponse
 */
public interface ScxHttpServerResponse {

    ScxHttpServerRequest request();

    HttpStatusCode status();

    ScxHttpHeadersWritable headers();

    ScxHttpServerResponse status(HttpStatusCode code);

    OutputStream outputStream();

    default void send(MediaWriter writer) {
        writer.beforeWrite(headers(), request().headers());
        writer.write(outputStream());
        end();
    }

    default void send() {
        end();
    }

    default void send(byte[] data) {
        send(new ByteArrayWriter(data));
    }

    default void send(String data) {
        send(new StringWriter(data));
    }

    void end();

    boolean isClosed();

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

}
