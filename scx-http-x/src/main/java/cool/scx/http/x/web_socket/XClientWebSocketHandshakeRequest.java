package cool.scx.http.x.web_socket;

import cool.scx.common.util.Base64Utils;
import cool.scx.common.util.RandomUtils;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.media.empty.EmptyWriter;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.http.web_socket.ScxClientWebSocketHandshakeRequest;
import cool.scx.http.web_socket.ScxClientWebSocketHandshakeResponse;
import cool.scx.http.x.XHttpClient;
import cool.scx.http.x.http1x.Http1xClientConnection;
import cool.scx.tcp.ScxTCPClient;
import cool.scx.tcp.ScxTCPSocket;

import static cool.scx.http.HttpFieldName.*;


/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class XClientWebSocketHandshakeRequest implements ScxClientWebSocketHandshakeRequest {

    private final XHttpClient httpClient;
    private final WebSocketOptions webSocketOptions;
    private WebSocketOptions options;
    private ScxURIWritable uri;
    private ScxTCPClient tcpClient;
    private ScxTCPSocket tcpSocket;
    private ScxHttpHeadersWritable headers;

    public XClientWebSocketHandshakeRequest(XHttpClient httpClient) {
        this.httpClient = httpClient;
        this.webSocketOptions = httpClient.options().webSocketOptions();
        this.uri = ScxURI.of();
        this.headers = ScxHttpHeaders.of();
    }

    @Override
    public ScxURIWritable uri() {
        return uri;
    }

    @Override
    public ScxHttpHeadersWritable headers() {
        return headers;
    }

    @Override
    public ScxClientWebSocketHandshakeRequest uri(ScxURI uri) {
        this.uri = ScxURI.of(uri);
        return this;
    }

    @Override
    public ScxClientWebSocketHandshakeRequest headers(ScxHttpHeaders headers) {
        this.headers = ScxHttpHeaders.of(headers);
        return this;
    }

    @Override
    public ScxClientWebSocketHandshakeResponse sendHandshake() {
        //0, 创建 tcp 连接
        this.tcpSocket = httpClient.createTCPSocket(uri, "http/1.1");

        //1, 创建 secWebsocketKey
        var secWebsocketKey = Base64Utils.encodeToString(RandomUtils.randomBytes(16));

        //2, 创建 请求头
        this.headers.add(CONNECTION, "Upgrade");
        this.headers.add(UPGRADE, "websocket");
        this.headers.add(SEC_WEBSOCKET_KEY, secWebsocketKey);
        this.headers.add(HOST, "127.0.0.1");
        this.headers.add(SEC_WEBSOCKET_VERSION, "13");

        var connection = new Http1xClientConnection(tcpSocket, httpClient.options());
        var response = connection.sendRequest(this, new EmptyWriter()).waitResponse();

        return new XClientWebSocketHandshakeResponse(connection, response, this.webSocketOptions);

    }

}
