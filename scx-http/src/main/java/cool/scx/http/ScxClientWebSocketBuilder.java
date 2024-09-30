package cool.scx.http;

import cool.scx.http.cookie.Cookie;
import cool.scx.http.uri.ScxURI;

import java.util.function.Consumer;

/**
 * ScxClientWebSocketBuilder
 */
public interface ScxClientWebSocketBuilder {

    ScxURI uri();

    ScxClientWebSocketBuilder uri(ScxURI uri);
    
    ScxHttpHeadersWritable headers();
    
    ScxClientWebSocketBuilder headers(ScxHttpHeadersWritable headers);

    ScxClientWebSocketBuilder onConnect(Consumer<ScxClientWebSocket> onOpen);

    void connect();

    default ScxClientWebSocketBuilder uri(String uri) {
        return uri(ScxURI.of(uri));
    }

    //************** 简化 Headers 操作 *************

    default ScxClientWebSocketBuilder setHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().set(headerName, values);
        return this;
    }

    default ScxClientWebSocketBuilder addHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().add(headerName, values);
        return this;
    }

    default ScxClientWebSocketBuilder setHeader(String headerName, String... values) {
        this.headers().set(headerName, values);
        return this;
    }

    default ScxClientWebSocketBuilder addHeader(String headerName, String... values) {
        this.headers().add(headerName, values);
        return this;
    }

    default ScxClientWebSocketBuilder addCookie(Cookie cookie) {
        headers().addCookie(cookie);
        return this;
    }

    default ScxClientWebSocketBuilder removeCookie(String name) {
        headers().removeCookie(name);
        return this;
    }

}
