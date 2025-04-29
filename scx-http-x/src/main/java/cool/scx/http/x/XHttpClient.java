package cool.scx.http.x;

import cool.scx.http.ScxHttpClient;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.x.http1.Http1ClientConnection;
import cool.scx.tcp.ScxTCPSocket;
import cool.scx.tcp.TCPClient;
import cool.scx.tcp.tls.TLS;

import java.io.IOException;
import java.io.UncheckedIOException;

import static cool.scx.http.media.empty.EmptyWriter.EMPTY_WRITER;
import static cool.scx.http.method.HttpMethod.CONNECT;
import static cool.scx.http.status.HttpStatus.OK;
import static cool.scx.http.x.XHttpClientHelper.checkIsTLS;
import static cool.scx.http.x.XHttpClientHelper.getRemoteAddress;
import static cool.scx.http.x.http1.request_line.RequestTargetForm.AUTHORITY_FORM;

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
        //判断是否 tls 
        var isTLS = checkIsTLS(uri);
        //判断是否使用代理
        var useProxy = options.proxy() != null && options.proxy().enabled();

        if (isTLS) {
            var tcpClient = new TCPClient(options.tcpClientOptions());
            ScxTCPSocket tcpSocket;
            TLS tls;
            // tls 模式下使用代理
            if (useProxy) {

                //1, 我们明文连接代理地址 
                var remoteAddress = options.proxy().proxyAddress();
                tcpSocket = tcpClient.connect(remoteAddress, options().timeout());

                //2, 和代理服务器 握手
                var proxyResponse = new Http1ClientConnection(tcpSocket, options).sendRequest(
                                (XHttpClientRequest) new XHttpClientRequest(this)
                                        .requestTargetForm(AUTHORITY_FORM)
                                        .method(CONNECT)
                                        .addHeader("proxy-connection", "keep-alive")
                                        .uri(uri),
                                EMPTY_WRITER
                        )
                        .waitResponse();
                //3, 握手成功 
                if (proxyResponse.status() != OK) {
                    throw new RuntimeException("代理连接失败 :" + proxyResponse.status());
                }
                //4, 这种情况下我们信任所有证书
                tls = TLS.ofTrustAny();
            } else {
                //配置一下 tls 为系统默认
                var remoteAddress = getRemoteAddress(uri);
                tcpSocket = tcpClient.connect(remoteAddress, options().timeout());
                tls = TLS.ofDefault();
            }
            //手动升级
            try {
                tcpSocket.upgradeToTLS(tls);
            } catch (IOException e) {
                throw new UncheckedIOException("升级 TLS 失败 !!!", e);
            }
            tcpSocket.tlsManager().setUseClientMode(true);
            tcpSocket.tlsManager().setApplicationProtocols(applicationProtocols);
            try {
                tcpSocket.startHandshake();
            } catch (IOException e) {
                throw new UncheckedIOException("握手失败 !!!", e);
            }
            return tcpSocket;
        }

        //不是 tls 但是启用代理
        if (useProxy) {
            var tcpClient = new TCPClient(options.tcpClientOptions());
            //我们连接代理地址 
            var remoteAddress = options.proxy().proxyAddress();
            return tcpClient.connect(remoteAddress, options.timeout());
        }

        //不是 tls 也不启用代理
        var tcpClient = new TCPClient(options.tcpClientOptions());
        var remoteAddress = getRemoteAddress(uri);
        return tcpClient.connect(remoteAddress, options.timeout());

    }

    @Override
    public XHttpClientRequest request() {
        return new XHttpClientRequest(this);
    }

}
