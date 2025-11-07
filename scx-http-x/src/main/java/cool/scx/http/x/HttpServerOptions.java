package cool.scx.http.x;

import cool.scx.http.x.http1.Http1ServerConnectionOptions;
import cool.scx.http.x.http1.Http1UpgradeHandler;
import cool.scx.tcp.TCPServerOptions;
import cool.scx.tcp.tls.TLS;

import java.util.List;

/// Http 服务器配置
///
/// @author scx567888
/// @version 0.0.1
public class HttpServerOptions {

    private final TCPServerOptions tcpServerOptions; // TCP 服务器配置
    private final Http1ServerConnectionOptions http1ConnectionOptions;// Http1 配置
    private TLS tls; // tls
    private boolean enableHttp2; // 是否开启 Http2

    public HttpServerOptions() {
        this.tcpServerOptions = new TCPServerOptions();
        this.http1ConnectionOptions = new Http1ServerConnectionOptions();
        this.tls = null; // 默认没有 tls
        this.enableHttp2 = false;// 默认不启用 http2
    }

    public HttpServerOptions(HttpServerOptions oldOptions) {
        this.tcpServerOptions = new TCPServerOptions(oldOptions.tcpServerOptions());
        this.http1ConnectionOptions = new Http1ServerConnectionOptions(oldOptions.http1ConnectionOptions());
        tls(oldOptions.tls());
        enableHttp2(oldOptions.enableHttp2());
    }

    // 因为涉及到一些底层实现, 所以不允许外界访问
    TCPServerOptions tcpServerOptions() {
        return tcpServerOptions;
    }

    public Http1ServerConnectionOptions http1ConnectionOptions() {
        return http1ConnectionOptions;
    }

    public int maxRequestLineSize() {
        return http1ConnectionOptions.maxRequestLineSize();
    }

    public HttpServerOptions maxRequestLineSize(int maxRequestLineSize) {
        http1ConnectionOptions.maxRequestLineSize(maxRequestLineSize);
        return this;
    }

    public int maxHeaderSize() {
        return http1ConnectionOptions.maxHeaderSize();
    }

    public HttpServerOptions maxHeaderSize(int maxHeaderSize) {
        http1ConnectionOptions.maxHeaderSize(maxHeaderSize);
        return this;
    }

    public long maxPayloadSize() {
        return http1ConnectionOptions.maxPayloadSize();
    }

    public HttpServerOptions maxPayloadSize(long maxPayloadSize) {
        http1ConnectionOptions.maxPayloadSize(maxPayloadSize);
        return this;
    }

    public boolean autoRespond100Continue() {
        return http1ConnectionOptions.autoRespond100Continue();
    }

    public HttpServerOptions autoRespond100Continue(boolean autoRespond100Continue) {
        http1ConnectionOptions.autoRespond100Continue(autoRespond100Continue);
        return this;
    }

    public boolean validateHost() {
        return http1ConnectionOptions.validateHost();
    }

    public HttpServerOptions validateHost(boolean validateHost) {
        http1ConnectionOptions.validateHost(validateHost);
        return this;
    }

    public List<Http1UpgradeHandler> upgradeHandlerList() {
        return http1ConnectionOptions.upgradeHandlerList();
    }

    public HttpServerOptions upgradeHandlerList(List<Http1UpgradeHandler> upgradeHandlerList) {
        http1ConnectionOptions.upgradeHandlerList(upgradeHandlerList);
        return this;
    }

    public HttpServerOptions addUpgradeHandler(Http1UpgradeHandler... upgradeHandlerList) {
        http1ConnectionOptions.addUpgradeHandler(upgradeHandlerList);
        return this;
    }

    public boolean enableHttp2() {
        return enableHttp2;
    }

    public HttpServerOptions enableHttp2(boolean enableHttp2) {
        this.enableHttp2 = enableHttp2;
        return this;
    }

    public int backlog() {
        return tcpServerOptions.backlog();
    }

    public HttpServerOptions backlog(int backlog) {
        this.tcpServerOptions.backlog(backlog);
        return this;
    }

    public TLS tls() {
        return tls;
    }

    public HttpServerOptions tls(TLS tls) {
        this.tls = tls;
        return this;
    }

}
