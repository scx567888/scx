package cool.scx.http;

import cool.scx.http.cookie.Cookie;

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

    boolean isClosed();

    ScxHttpServerResponse addCookie(Cookie cookie);
    
    ScxHttpServerResponse removeCookie(String name);

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

}
