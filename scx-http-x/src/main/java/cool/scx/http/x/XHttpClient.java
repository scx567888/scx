package cool.scx.http.x;

import cool.scx.http.ScxHttpClient;
import cool.scx.http.uri.ScxURI;
import cool.scx.tcp.ScxTCPSocket;
import cool.scx.tcp.TCPClient;
import cool.scx.tcp.TCPClientOptions;
import cool.scx.tcp.tls.TLS;

import java.io.IOException;
import java.net.SocketAddress;

import static cool.scx.http.x.XHttpClientHelper.checkIsTLS;
import static cool.scx.http.x.XHttpClientHelper.getRemoteAddress;

/// todo 待完成
///
/// @author scx567888
/// @version 0.0.1
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
            tcpClientOptions = new TCPClientOptions(tcpClientOptions).tls(TLS.ofDefault());
        }

        var tcpClient = new TCPClient(tcpClientOptions);

        SocketAddress remoteAddress;
        //这里如果没有代理就使用 uri 作为远端地址, 否则使用代理地址
        if (options.proxy() != null && options.proxy().enabled()) {
            remoteAddress = options.proxy().proxyAddress();
        } else {
            remoteAddress = getRemoteAddress(uri);
        }

        var tcpSocket = tcpClient.connect(remoteAddress, options().timeout());

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
    public XHttpClientRequest request() {
        return new XHttpClientRequest(this);
    }

}
