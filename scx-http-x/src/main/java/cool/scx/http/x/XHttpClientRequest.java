package cool.scx.http.x;

import cool.scx.http.ScxHttpClientRequestBase;
import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.http.x.http1x.Http1xClientConnection;
import cool.scx.http.x.http2.Http2xClientConnection;
import cool.scx.tcp.*;
import cool.scx.tcp.tls.TLS;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class XHttpClientRequest extends ScxHttpClientRequestBase {

    private final XHttpClient httpClient;
    private final XHttpClientOptions options;
    public ScxTCPSocket tcpSocket;
    ScxTCPClient tcpClient;

    public XHttpClientRequest(XHttpClient httpClient) {
        this.httpClient = httpClient;
        this.options = httpClient.options();
    }

    public static InetSocketAddress getRemoteAddress(ScxURI uri) {
        var defaultPort = -1;
        if ("http".equals(uri.scheme())) {
            defaultPort = 80;
        } else if ("https".equals(uri.scheme())) {
            defaultPort = 443;
        } else {
            throw new IllegalArgumentException("Unsupported scheme: " + uri.scheme());
        }
        var port = uri.port() == -1 ? defaultPort : uri.port();

        return new InetSocketAddress(uri.host(), port);
    }


    private static boolean checkIsHttps(ScxURIWritable uri) {
        if ("http".equals(uri.scheme())) {
            return false;
        } else if ("https".equals(uri.scheme())) {
            return true;
        } else {
            throw new IllegalArgumentException("Unsupported scheme: " + uri.scheme());
        }
    }

    public static TLS getTrustAllTLS() {
        // 创建自定义 TrustManager，忽略证书验证（仅用于测试环境）
        var trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            // 初始化 SSLContext
            var sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null);
            return new TLS(sslContext);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ScxHttpClientResponse send(MediaWriter writer) {
        var isHttps = checkIsHttps(uri);

        var tcpClientOptions = httpClient.options().tcpClientOptions();

        if (isHttps) {
            tcpClientOptions = new ScxTCPClientOptions(tcpClientOptions).tls(getTrustAllTLS());
        }

        this.tcpClient = switch (httpClient.options().tcpClientType()) {
            case CLASSIC -> new ClassicTCPClient(tcpClientOptions);
            case NIO -> new NioTCPClient(tcpClientOptions);
        };

        var remoteAddress = getRemoteAddress(uri);
        this.tcpSocket = tcpClient.connect(remoteAddress);

        var useHttp2 = false;

        if (this.tcpSocket.isTLS()) {
            this.tcpSocket.tlsManager().setApplicationProtocols(getApplicationProtocols());
            try {
                this.tcpSocket.startHandshake();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            var applicationProtocol = this.tcpSocket.tlsManager().getApplicationProtocol();
            useHttp2 = "h2".equals(applicationProtocol);
        }

        if (useHttp2) {
            return new Http2xClientConnection(tcpSocket, options).sendRequest(this, writer).waitResponse();
        } else {
            return new Http1xClientConnection(tcpSocket, options).sendRequest(this, writer).waitResponse();
        }

    }

    private String[] getApplicationProtocols() {
        if (this.options.enableHttp2()) {
            return new String[]{"http/1.1", "h2"};
        } else {
            return new String[]{"http/1.1"};
        }
    }

}
