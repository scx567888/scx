package cool.scx.websocket.x;

import cool.scx.common.util.Base64Utils;
import cool.scx.common.util.RandomUtils;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.http.x.HttpClient;
import cool.scx.http.x.http1.Http1ClientConnection;
import cool.scx.http.x.http1.Http1ClientRequest;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.request_line.RequestTargetForm;
import cool.scx.websocket.ScxClientWebSocketHandshakeRequest;
import cool.scx.websocket.ScxClientWebSocketHandshakeResponse;

import static cool.scx.http.headers.HttpFieldName.SEC_WEBSOCKET_KEY;
import static cool.scx.http.headers.HttpFieldName.SEC_WEBSOCKET_VERSION;
import static cool.scx.http.media.empty.EmptyWriter.EMPTY_WRITER;
import static cool.scx.http.x.http1.headers.connection.Connection.UPGRADE;
import static cool.scx.http.x.http1.headers.upgrade.Upgrade.WEB_SOCKET;
import static cool.scx.http.x.http1.request_line.RequestTargetForm.ABSOLUTE_FORM;
import static cool.scx.http.x.http1.request_line.RequestTargetForm.ORIGIN_FORM;


/// ClientWebSocketHandshakeRequest
///
/// @author scx567888
/// @version 0.0.1
public class ClientWebSocketHandshakeRequest implements ScxClientWebSocketHandshakeRequest, Http1ClientRequest {

    private final HttpClient httpClient;
    private final WebSocketOptions webSocketOptions;
    private ScxURIWritable uri;
    private Http1Headers headers;
    private RequestTargetForm requestTargetForm;

    public ClientWebSocketHandshakeRequest(HttpClient httpClient, WebSocketOptions webSocketOptions) {
        this.httpClient = httpClient;
        this.webSocketOptions = webSocketOptions;
        this.uri = ScxURI.of();
        this.headers = new Http1Headers();
        this.requestTargetForm = ORIGIN_FORM;
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

        //仅当 http 协议并且开启代理的时候才使用 绝对路径
        if (!tcpSocket.isTLS() && httpClient.options().proxy() != null && httpClient.options().proxy().enabled()) {
            this.requestTargetForm = ABSOLUTE_FORM;
        }
        var connection = new Http1ClientConnection(tcpSocket, httpClient.options());
        var response = connection.sendRequest(this, EMPTY_WRITER).waitResponse();

        return new ClientWebSocketHandshakeResponse(connection, response, this.webSocketOptions);

    }

    @Override
    public RequestTargetForm requestTargetForm() {
        return this.requestTargetForm;
    }

    @Override
    public Http1ClientRequest requestTargetForm(RequestTargetForm requestTargetForm) {
        this.requestTargetForm = requestTargetForm;
        return this;
    }

}
