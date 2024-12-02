package cool.scx.http;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.cookie.Cookie;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media.byte_array.ByteArrayWriter;
import cool.scx.http.media.empty.EmptyWriter;
import cool.scx.http.media.form_params.FormParams;
import cool.scx.http.media.form_params.FormParamsWriter;
import cool.scx.http.media.input_stream.InputStreamWriter;
import cool.scx.http.media.json_node.JsonNodeWriter;
import cool.scx.http.media.multi_part.MultiPart;
import cool.scx.http.media.multi_part.MultiPartWriter;
import cool.scx.http.media.object.ObjectWriter;
import cool.scx.http.media.path.PathWriter;
import cool.scx.http.media.string.StringWriter;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * ScxHttpServerResponse
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxHttpServerResponse {

    // ************* 基本方法 ****************
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

    //************** 简化操作 ***************

    default ScxHttpServerResponse status(int code) {
        return status(HttpStatusCode.of(code));
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

    default void send(FormParams formParams) {
        send(new FormParamsWriter(formParams));
    }

    default void send(MultiPart multiPart) {
        send(new MultiPartWriter(multiPart));
    }

    default void send(JsonNode jsonNode) {
        send(new JsonNodeWriter(jsonNode));
    }

    default void send(Object object) {
        send(new ObjectWriter(object));
    }

    //************** 简化 Headers 操作 *************

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

    default ContentType contentType() {
        return headers().contentType();
    }

    default ScxHttpServerResponse contentType(ContentType contentType) {
        headers().contentType(contentType);
        return this;
    }

    default ScxHttpServerResponse contentType(ScxMediaType mediaType) {
        headers().contentType(mediaType);
        return this;
    }

    default ScxHttpServerResponse contentType(ScxMediaType mediaType, Charset charset) {
        headers().contentType(mediaType, charset);
        return this;
    }

    default Long contentLength() {
        return headers().contentLength();
    }

    default ScxHttpServerResponse contentLength(long contentLength) {
        headers().contentLength(contentLength);
        return this;
    }

}
