package cool.scx.http;

import cool.scx.http.content_type.ContentType;

import java.io.OutputStream;

/**
 * ScxHttpServerResponse
 */
public interface ScxHttpServerResponse {

    HttpStatusCode status();

    ScxHttpServerResponseHeaders headers();

    ScxHttpServerResponse status(HttpStatusCode code);

    OutputStream outputStream();

    void send();

    void send(byte[] data);

    void send(String data);

    void send(Object data);

    boolean isClosed();

    default ScxHttpServerResponse setHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().set(headerName, values);
        return this;
    }

    default ScxHttpServerResponse addHeader(ScxHttpHeaderName headerName, String... values) {
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

    default ScxHttpServerResponse contentType(MediaType mediaType) {
        headers().contentType(mediaType);
        return this;
    }

}
