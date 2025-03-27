package cool.scx.http;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.http.headers.ScxHttpHeaderName;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.headers.cookie.Cookie;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media.byte_array.ByteArrayWriter;
import cool.scx.http.media.event_stream.ServerEventStream;
import cool.scx.http.media.event_stream.ServerEventStreamWriter;
import cool.scx.http.media.form_params.FormParams;
import cool.scx.http.media.form_params.FormParamsWriter;
import cool.scx.http.media.input_stream.InputStreamWriter;
import cool.scx.http.media.json_node.JsonNodeWriter;
import cool.scx.http.media.multi_part.MultiPart;
import cool.scx.http.media.multi_part.MultiPartWriter;
import cool.scx.http.media.object.ObjectWriter;
import cool.scx.http.media.path.PathWriter;
import cool.scx.http.media.string.StringWriter;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.status.ScxHttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

import static cool.scx.http.media.empty.EmptyWriter.EMPTY_WRITER;

/// ScxHttpServerResponse
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpServerResponse {

    ScxHttpServerRequest request();

    ScxHttpStatus status();

    ScxHttpHeadersWritable headers();

    ScxHttpServerResponse status(ScxHttpStatus code);

    /// 获取输出流
    ///
    /// @param expectedLength 预期的内容长度 : (-1 未知长度, 0 无内容, 大于 0 标准长度)
    OutputStream outputStream(long expectedLength);

    boolean isSent();

    default OutputStream outputStream() {
        return outputStream(-1);
    }

    //******************** send 操作 *******************

    default void send(MediaWriter writer) {
        var expectedLength = writer.beforeWrite(headers(), request().headers());
        try {
            writer.write(outputStream(expectedLength));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    default void send() {
        send(EMPTY_WRITER);
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

    default ServerEventStream sendEventStream() {
        var writer = new ServerEventStreamWriter();
        send(writer);
        return writer.eventStream();
    }

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

}
