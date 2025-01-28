package cool.scx.http.x;

import cool.scx.http.ScxHttpClient;
import cool.scx.http.ScxHttpClientRequest;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.web_socket.ScxClientWebSocketHandshakeRequest;
import cool.scx.http.x.web_socket.XClientWebSocketHandshakeRequest;
import cool.scx.tcp.*;
import cool.scx.tcp.tls.TLS;

import java.io.IOException;

import static cool.scx.http.x.XHttpClientHelper.checkIsTLS;
import static cool.scx.http.x.XHttpClientHelper.getRemoteAddress;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class XHttpClient implements ScxHttpClient {

    private final XHttpClientOptions options;

    public XHttpClient(XHttpClientOptions options) {
        this.options = options;
    }

    public XHttpClient() {
        this(new XHttpClientOptions());
    }

    public XHttpClientOptions options() {
        return options;
    }

    //创建一个 TCP 连接 todo 后期可以创建一个 连接池 用来复用 未断开的 tcp 连接
    public ScxTCPSocket createTCPSocket(ScxURI uri, String... applicationProtocols) {
        var isTLS = checkIsTLS(uri);

        var tcpClientOptions = options.tcpClientOptions();

        if (isTLS) {
            tcpClientOptions = new ScxTCPClientOptions(tcpClientOptions).tls(TLS.ofDefault());
        }

        var tcpClient = switch (options.tcpClientType()) {
            case CLASSIC -> new ClassicTCPClient(tcpClientOptions);
            case NIO -> new NioTCPClient(tcpClientOptions);
            case ASYNC -> new AsyncTCPClient(tcpClientOptions);
        };

        var remoteAddress = getRemoteAddress(uri);
        var tcpSocket = tcpClient.connect(remoteAddress);

        if (tcpSocket.isTLS()) {
            tcpSocket.tlsManager().setApplicationProtocols(applicationProtocols);
            try {
                tcpSocket.startHandshake();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return tcpSocket;
    }

    @Override
    public ScxHttpClientRequest request() {
        return new XHttpClientRequest(this);
    }

    @Override
    public ScxClientWebSocketHandshakeRequest webSocketHandshakeRequest() {
        return new XClientWebSocketHandshakeRequest(this);
    }

}
