package cool.scx.websocket.x;

import cool.scx.common.util.RandomUtils;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.sender.HttpSendException;
import cool.scx.http.sender.IllegalSenderStateException;
import cool.scx.http.sender.ScxHttpSenderStatus;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.http.x.HttpClient;
import cool.scx.http.x.http1.Http1ClientConnection;
import cool.scx.http.x.http1.Http1ClientRequest;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.request_line.RequestTargetForm;
import cool.scx.websocket.ScxClientWebSocketHandshakeRequest;
import cool.scx.websocket.ScxClientWebSocketHandshakeResponse;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.Socket;
import java.util.Base64;
import java.util.concurrent.locks.ReentrantLock;

import static cool.scx.http.headers.HttpHeaderName.SEC_WEBSOCKET_KEY;
import static cool.scx.http.headers.HttpHeaderName.SEC_WEBSOCKET_VERSION;
import static cool.scx.http.media.empty.EmptyMediaWriter.EMPTY_MEDIA_WRITER;
import static cool.scx.http.sender.ScxHttpSenderStatus.NOT_SENT;
import static cool.scx.http.version.HttpVersion.HTTP_1_1;
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
    private final ReentrantLock sendLock;
    private ScxURIWritable uri;
    private Http1Headers headers;
    private RequestTargetForm requestTargetForm;
    private ScxHttpSenderStatus senderStatus;

    public ClientWebSocketHandshakeRequest(HttpClient httpClient, WebSocketOptions webSocketOptions) {
        this.httpClient = httpClient;
        this.webSocketOptions = webSocketOptions;
        this.sendLock = new ReentrantLock();
        this.uri = ScxURI.of();
        this.headers = new Http1Headers();
        this.requestTargetForm = ORIGIN_FORM;
        this.senderStatus = NOT_SENT;
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

    private ScxClientWebSocketHandshakeResponse sendHandshake0() {

        // 检查发送状态
        if (senderStatus != NOT_SENT) {
            throw new IllegalSenderStateException(senderStatus);
        }

        //0, 创建 tcp 连接
        Socket tcpSocket;

        try {
            tcpSocket = httpClient.createTCPSocket(uri, HTTP_1_1.alpnValue());
        } catch (IOException e) {
            throw new HttpSendException("创建连接失败 !!!", e);
        }

        //1, 创建 secWebsocketKey
        var secWebsocketKey = Base64.getEncoder().encodeToString(RandomUtils.randomBytes(16));

        //2, 创建 请求头
        this.headers.connection(UPGRADE);
        this.headers.upgrade(WEB_SOCKET);
        this.headers.set(SEC_WEBSOCKET_KEY, secWebsocketKey);
        this.headers.set(SEC_WEBSOCKET_VERSION, "13");

        //仅当 http 协议并且开启代理的时候才使用 绝对路径
        if (!(tcpSocket instanceof SSLSocket) && httpClient.options().proxy() != null) {
            this.requestTargetForm = ABSOLUTE_FORM;
        }

        try {
            var connection = new Http1ClientConnection(tcpSocket, httpClient.options().http1ClientConnectionOptions());
            var response = connection.sendRequest(this, EMPTY_MEDIA_WRITER).waitResponse();
            return new ClientWebSocketHandshakeResponse(connection, response, this.webSocketOptions);
        } catch (IOException e) {
            throw new HttpSendException("发送 WebSocket 握手请求失败 !!!", e);
        }
    }

    @Override
    public ScxClientWebSocketHandshakeResponse sendHandshake() {
        sendLock.lock();
        try {
            return sendHandshake0();
        } finally {
            sendLock.unlock();
        }
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

    @Override
    public void _setSenderStatus(ScxHttpSenderStatus senderStatus) {
        this.senderStatus = senderStatus;
    }

    @Override
    public ScxHttpSenderStatus senderStatus() {
        return senderStatus;
    }

}
