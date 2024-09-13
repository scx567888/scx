package cool.scx.http;

import java.io.OutputStream;

public interface ScxHttpServerResponse {

    HttpStatusCode statusCode();

    ScxHttpHeadersWritable headers();

    ScxHttpServerResponse setStatusCode(HttpStatusCode code);

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

}
