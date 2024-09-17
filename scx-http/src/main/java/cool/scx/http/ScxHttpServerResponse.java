package cool.scx.http;

import java.io.OutputStream;

/**
 * ScxHttpServerResponse
 */
public interface ScxHttpServerResponse {

    HttpStatusCode status();

    ScxHttpHeadersWritable headers();

    ScxHttpServerResponse status(HttpStatusCode code);

    OutputStream outputStream();

    void send();

    void send(byte[] data);

    void send(String data);

    void send(Object data);

    boolean closed();

    default ScxHttpServerResponse setHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().set(headerName, values);
        return this;
    }

    default ScxHttpServerResponse addHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().add(headerName, values);
        return this;
    }

}
