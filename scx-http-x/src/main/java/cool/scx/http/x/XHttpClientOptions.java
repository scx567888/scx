package cool.scx.http.x;

import cool.scx.http.x.http1.Http1ClientConnectionOptions;
import cool.scx.http.x.proxy.Proxy;
import cool.scx.tcp.TCPClientOptions;
import cool.scx.tcp.tls.TLS;

/// XHttpClientOptions
///
/// @author scx567888
/// @version 0.0.1
public class XHttpClientOptions {

    private final TCPClientOptions tcpClientOptions;// TCP 客户端 配置
    private final Http1ClientConnectionOptions http1ConnectionOptions;// Http1 配置
    private Proxy proxy;// 代理功能
    private int timeout;// 超时设置
    private boolean enableHttp2; // 是否开启 Http2

    public XHttpClientOptions() {
        //默认 不自动升级也不自动握手 已实现代理
        this.tcpClientOptions = new TCPClientOptions().autoUpgradeToTLS(false).autoHandshake(false);
        this.http1ConnectionOptions = new Http1ClientConnectionOptions();
        this.proxy = null;//默认不启用代理
        this.timeout = 10 * 1000;//默认 10 秒
        this.enableHttp2 = false;//默认不启用 http2
    }

    public XHttpClientOptions(XHttpClientOptions oldOptions) {
        //默认 不自动升级也不自动握手 已实现代理
        this.tcpClientOptions = new TCPClientOptions(oldOptions.tcpClientOptions()).autoUpgradeToTLS(false).autoHandshake(false);
        this.http1ConnectionOptions = new Http1ClientConnectionOptions(oldOptions.http1ConnectionOptions());
        proxy(oldOptions.proxy());
        timeout(oldOptions.timeout());
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

    public Proxy proxy() {
        return proxy;
    }

    public XHttpClientOptions proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public int timeout() {
        return timeout;
    }

    public XHttpClientOptions timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

}
