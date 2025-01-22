package cool.scx.http.x.http1x;

import cool.scx.http.ScxHttpBody;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeResponse;

/**
 * 基于 http1 的 websocket 握手请求
 *
 * @author scx567888
 * @version 0.0.1
 */
public class Http1xServerWebSocketHandshakeRequest extends AbstractHttp1xServerRequest implements ScxServerWebSocketHandshakeRequest {

    private final Http1xServerWebSocketHandshakeResponse response;

    public Http1xServerWebSocketHandshakeRequest(Http1xServerConnection connection, Http1xRequestLine requestLine, ScxHttpHeadersWritable headers, ScxHttpBody body) {
        super(connection, requestLine, headers, body);
        this.response = new Http1xServerWebSocketHandshakeResponse(connection, this);
    }

    @Override
    public ScxServerWebSocketHandshakeResponse response() {
        return this.response;
    }

}
