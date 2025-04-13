package cool.scx.websocket.x;

import cool.scx.common.util.Base64Utils;
import cool.scx.common.util.RandomUtils;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.http.x.XHttpClient;
import cool.scx.http.x.http1.Http1ClientConnection;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.tcp.ScxTCPClient;
import cool.scx.websocket.ScxClientWebSocketHandshakeRequest;
import cool.scx.websocket.ScxClientWebSocketHandshakeResponse;

import static cool.scx.http.headers.HttpFieldName.SEC_WEBSOCKET_KEY;
import static cool.scx.http.headers.HttpFieldName.SEC_WEBSOCKET_VERSION;
import static cool.scx.http.media.empty.EmptyWriter.EMPTY_WRITER;
import static cool.scx.http.x.http1.headers.connection.Connection.UPGRADE;
import static cool.scx.http.x.http1.headers.upgrade.Upgrade.WEB_SOCKET;


/// todo 待完成
///
/// @author scx567888
/// @version 0.0.1
public class XClientWebSocketHandshakeRequest implements ScxClientWebSocketHandshakeRequest {

    private final XHttpClient httpClient;
    private final WebSocketOptions webSocketOptions;
    private WebSocketOptions options;
    private ScxURIWritable uri;
    private ScxTCPClient tcpClient;
//    private ScxTCPSocket tcpSocket; //todo 和 XHttpClientRequest 中一样 有必要持有一个吗?
    private Http1Headers headers;

    public XClientWebSocketHandshakeRequest(XWebSocketClient httpClient, WebSocketOptions webSocketOptions) {
        this.httpClient = httpClient;
        this.webSocketOptions = webSocketOptions;
        this.uri = ScxURI.of();
        this.headers = new Http1Headers();
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
        this.headers = new Http1Headers(headers);
        return this;
    }

    @Override
    public ScxClientWebSocketHandshakeResponse sendHandshake() {
        //0, 创建 tcp 连接
        var tcpSocket = httpClient.createTCPSocket(uri, "http/1.1");

        //1, 创建 secWebsocketKey
        var secWebsocketKey = Base64Utils.encodeToString(RandomUtils.randomBytes(16));

        //2, 创建 请求头
        this.headers.connection(UPGRADE);
        this.headers.upgrade(WEB_SOCKET);
        this.headers.set(SEC_WEBSOCKET_KEY, secWebsocketKey);
        this.headers.set(SEC_WEBSOCKET_VERSION, "13");

        var connection = new Http1ClientConnection(tcpSocket, httpClient.options());
        var response = connection.sendRequest(this, EMPTY_WRITER).waitResponse();

        return new XClientWebSocketHandshakeResponse(connection, response, this.webSocketOptions);

    }

}
