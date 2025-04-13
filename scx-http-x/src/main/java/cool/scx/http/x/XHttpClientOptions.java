package cool.scx.http.x;

import cool.scx.http.x.http1.Http1ClientConnectionOptions;
import cool.scx.tcp.ScxTCPClientOptions;
import cool.scx.tcp.tls.TLS;

/// XHttpClientOptions
///
/// @author scx567888
/// @version 0.0.1
public class XHttpClientOptions {

    private final ScxTCPClientOptions tcpClientOptions;// TCP 客户端 配置
    private final Http1ClientConnectionOptions http1ConnectionOptions;// Http1 配置
    private TCPClientType tcpClientType;// TCP 客户端类型
    private boolean enableHttp2; // 是否开启 Http2

    public XHttpClientOptions() {
        this.tcpClientOptions = new ScxTCPClientOptions().autoUpgradeToTLS(true).autoHandshake(false);
        this.http1ConnectionOptions = new Http1ClientConnectionOptions();
        this.tcpClientType = TCPClientType.CLASSIC;
        this.enableHttp2 = false;//默认不启用 http2 
    }

    public XHttpClientOptions(XHttpClientOptions oldOptions) {
        this.tcpClientOptions = new ScxTCPClientOptions(oldOptions.tcpClientOptions()).autoUpgradeToTLS(true).autoHandshake(false);
        this.http1ConnectionOptions = new Http1ClientConnectionOptions(oldOptions.http1ConnectionOptions());
        tcpClientType(oldOptions.tcpClientType());
        enableHttp2(oldOptions.enableHttp2());
    }

    public Http1ClientConnectionOptions http1ConnectionOptions() {
        return http1ConnectionOptions;
    }

    ScxTCPClientOptions tcpClientOptions() {
        return tcpClientOptions;
    }

    public TCPClientType tcpClientType() {
        return tcpClientType;
    }

    public XHttpClientOptions tcpClientType(TCPClientType tcpClientType) {
        this.tcpClientType = tcpClientType;
        return this;
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

    public enum TCPClientType {
        CLASSIC,
        NIO
    }

}
