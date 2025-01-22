package cool.scx.http.web_socket;

import cool.scx.http.*;
import cool.scx.http.cookie.Cookie;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.uri.ScxURI;

/**
 * ScxClientWebSocketHandshakeRequest
 * 1, WebSocket 协议中指定了 必须由 GET 方法 和 空请求体 所以我们这里屏蔽掉一些方法
 * 2, 重写一些方法的返回值 方便我们链式调用
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxClientWebSocketHandshakeRequest extends ScxHttpClientRequest {

    @Override
    ScxClientWebSocketHandshakeRequest uri(ScxURI uri);

    @Override
    ScxClientWebSocketHandshakeRequest headers(ScxHttpHeaders headers);

    @Override
    ScxClientWebSocketHandshakeResponse send();

    @Override
    default ScxClientWebSocketHandshakeRequest uri(String uri) {
        return (ScxClientWebSocketHandshakeRequest) ScxHttpClientRequest.super.uri(uri);
    }

    @Override
    default ScxClientWebSocketHandshakeRequest setHeader(ScxHttpHeaderName headerName, String... values) {
        return (ScxClientWebSocketHandshakeRequest) ScxHttpClientRequest.super.setHeader(headerName, values);
    }

    @Override
    default ScxClientWebSocketHandshakeRequest addHeader(ScxHttpHeaderName headerName, String... values) {
        return (ScxClientWebSocketHandshakeRequest) ScxHttpClientRequest.super.setHeader(headerName, values);
    }

    @Override
    default ScxClientWebSocketHandshakeRequest setHeader(String headerName, String... values) {
        return (ScxClientWebSocketHandshakeRequest) ScxHttpClientRequest.super.setHeader(headerName, values);
    }

    @Override
    default ScxClientWebSocketHandshakeRequest addHeader(String headerName, String... values) {
        return (ScxClientWebSocketHandshakeRequest) ScxHttpClientRequest.super.addHeader(headerName, values);
    }

    @Override
    default ScxClientWebSocketHandshakeRequest addCookie(Cookie... cookie) {
        return (ScxClientWebSocketHandshakeRequest) ScxHttpClientRequest.super.addCookie(cookie);
    }

    @Override
    default ScxClientWebSocketHandshakeRequest removeCookie(String name) {
        return (ScxClientWebSocketHandshakeRequest) ScxHttpClientRequest.super.removeCookie(name);
    }

    //************ 屏蔽方法 ****************
    @Override
    default HttpVersion version() {
        return null;
    }

    @Override
    default ScxHttpMethod method() {
        return null;
    }

    @Override
    default ScxHttpClientRequest version(HttpVersion version) {
        throw new UnsupportedOperationException("Not supported Custom HttpVersion.");
    }

    @Override
    default ScxHttpClientRequest method(HttpMethod method) {
        throw new UnsupportedOperationException("Not supported Custom HttpMethod.");
    }

    @Override
    default ScxHttpClientResponse send(MediaWriter writer) {
        throw new UnsupportedOperationException("Not supported Custom HttpBody.");
    }

}
