package cool.scx.http.x;

import cool.scx.http.x.http1.Http1ClientConnectionOptions;
import cool.scx.http.x.proxy.Proxy;
import cool.scx.tcp.tls.TLS;

/// HttpClientOptions
///
/// @author scx567888
/// @version 0.0.1
public class HttpClientOptions {

    private final Http1ClientConnectionOptions http1ConnectionOptions;// Http1 配置
    private TLS tls;// TLS 配置
    private Proxy proxy;// 代理功能
    private int timeout;// 超时设置
    private boolean enableHttp2; // 是否开启 Http2

    public HttpClientOptions() {
        this.http1ConnectionOptions = new Http1ClientConnectionOptions();
        this.tls = TLS.ofDefault();//默认使用 系统 TLS
        this.proxy = null;//默认不启用代理
        this.timeout = 10 * 1000;//默认 10 秒
        this.enableHttp2 = false;//默认不启用 http2
    }

    public HttpClientOptions(HttpClientOptions oldOptions) {
        this.http1ConnectionOptions = new Http1ClientConnectionOptions(oldOptions.http1ConnectionOptions());
        tls(oldOptions.tls());
        proxy(oldOptions.proxy());
        timeout(oldOptions.timeout());
        enableHttp2(oldOptions.enableHttp2());
    }

    public Http1ClientConnectionOptions http1ConnectionOptions() {
        return http1ConnectionOptions;
    }

    public boolean enableHttp2() {
        return enableHttp2;
    }

    public HttpClientOptions enableHttp2(boolean enableHttp2) {
        this.enableHttp2 = enableHttp2;
        return this;
    }

    public TLS tls() {
        return tls;
    }

    public HttpClientOptions tls(TLS tls) {
        this.tls=tls;
        return this;
    }

    public Proxy proxy() {
        return proxy;
    }

    public HttpClientOptions proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public int timeout() {
        return timeout;
    }

    public HttpClientOptions timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

}
