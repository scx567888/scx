package cool.scx.http.x;

import cool.scx.http.x.http1.Http1ClientConnectionOptions;
import cool.scx.tcp.TCPClientOptions;
import cool.scx.tcp.tls.TLS;

/// XHttpClientOptions
///
/// @author scx567888
/// @version 0.0.1
public class XHttpClientOptions {

    private final TCPClientOptions tcpClientOptions;// TCP 客户端 配置
    private final Http1ClientConnectionOptions http1ConnectionOptions;// Http1 配置
    private boolean enableHttp2; // 是否开启 Http2

    public XHttpClientOptions() {
        this.tcpClientOptions = new TCPClientOptions().autoUpgradeToTLS(true).autoHandshake(false);
        this.http1ConnectionOptions = new Http1ClientConnectionOptions();
        this.enableHttp2 = false;//默认不启用 http2
    }

    public XHttpClientOptions(XHttpClientOptions oldOptions) {
        this.tcpClientOptions = new TCPClientOptions(oldOptions.tcpClientOptions()).autoUpgradeToTLS(true).autoHandshake(false);
        this.http1ConnectionOptions = new Http1ClientConnectionOptions(oldOptions.http1ConnectionOptions());
        enableHttp2(oldOptions.enableHttp2());
    }

    public Http1ClientConnectionOptions http1ConnectionOptions() {
        return http1ConnectionOptions;
    }

    TCPClientOptions tcpClientOptions() {
        return tcpClientOptions;
    }

    public boolean enableHttp2() {
        return enableHttp2;
    }

    public XHttpClientOptions enableHttp2(boolean enableHttp2) {
        this.enableHttp2 = enableHttp2;
        return this;
    }

    public TLS tls() {
        return tcpClientOptions.tls();
    }

    public XHttpClientOptions tls(TLS tls) {
        this.tcpClientOptions.tls(tls);
        return this;
    }

}
