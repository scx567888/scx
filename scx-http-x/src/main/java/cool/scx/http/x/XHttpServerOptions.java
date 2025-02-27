package cool.scx.http.x;

import cool.scx.http.x.http1x.Http1xServerConnectionOptions;
import cool.scx.http.x.web_socket.WebSocketOptions;
import cool.scx.tcp.ScxTCPServerOptions;
import cool.scx.tcp.tls.TLS;

import java.net.InetSocketAddress;

/// Http 服务器配置
///
/// @author scx567888
/// @version 0.0.1
public class XHttpServerOptions {

    private final ScxTCPServerOptions tcpServerOptions; // TCP 服务器配置
    private final Http1xServerConnectionOptions http1xConnectionOptions;// Http1 配置
    private final WebSocketOptions webSocketOptions;// WebSocket 配置
    private TCPServerType tcpServerType; // TCP 服务器类型
    private boolean enableHttp2; // 是否开启 Http2

    public XHttpServerOptions() {
        //默认不自动握手
        this.tcpServerOptions = new ScxTCPServerOptions().autoUpgradeToTLS(true).autoHandshake(false);
        this.http1xConnectionOptions = new Http1xServerConnectionOptions();
        this.webSocketOptions = new WebSocketOptions();
        this.tcpServerType = TCPServerType.CLASSIC; // 默认 使用 CLASSIC 实现
        this.enableHttp2 = false;//默认不启用 http2
    }

    public XHttpServerOptions(XHttpServerOptions oldOptions) {
        //默认不自动握手
        this.tcpServerOptions = new ScxTCPServerOptions(oldOptions.tcpServerOptions()).autoUpgradeToTLS(true).autoHandshake(false);
        this.http1xConnectionOptions = new Http1xServerConnectionOptions(oldOptions.http1xConnectionOptions());
        this.webSocketOptions = new WebSocketOptions(oldOptions.webSocketOptions());
        tcpServerType(oldOptions.tcpServerType());
        enableHttp2(oldOptions.enableHttp2());
    }

    //因为涉及到一些底层实现, 所以不允许外界访问
    ScxTCPServerOptions tcpServerOptions() {
        return tcpServerOptions;
    }

    public Http1xServerConnectionOptions http1xConnectionOptions() {
        return http1xConnectionOptions;
    }

    public WebSocketOptions webSocketOptions() {
        return webSocketOptions;
    }

    public TCPServerType tcpServerType() {
        return tcpServerType;
    }

    public XHttpServerOptions tcpServerType(TCPServerType tcpServerType) {
        this.tcpServerType = tcpServerType;
        return this;
    }

    public int maxRequestLineSize() {
        return http1xConnectionOptions.maxRequestLineSize();
    }

    public XHttpServerOptions maxRequestLineSize(int maxRequestLineSize) {
        http1xConnectionOptions.maxRequestLineSize(maxRequestLineSize);
        return this;
    }

    public int maxHeaderSize() {
        return http1xConnectionOptions.maxHeaderSize();
    }

    public XHttpServerOptions maxHeaderSize(int maxHeaderSize) {
        http1xConnectionOptions.maxHeaderSize(maxHeaderSize);
        return this;
    }

    public long maxPayloadSize() {
        return http1xConnectionOptions.maxPayloadSize();
    }

    public XHttpServerOptions maxPayloadSize(long maxPayloadSize) {
        http1xConnectionOptions.maxPayloadSize(maxPayloadSize);
        return this;
    }

    public boolean autoRespond100Continue() {
        return http1xConnectionOptions.autoRespond100Continue();
    }

    public XHttpServerOptions autoRespond100Continue(boolean autoRespond100Continue) {
        http1xConnectionOptions.autoRespond100Continue(autoRespond100Continue);
        return this;
    }

    public boolean mergeWebSocketFrame() {
        return webSocketOptions.mergeWebSocketFrame();
    }

    public XHttpServerOptions mergeWebSocketFrame(boolean mergeWebSocketFrame) {
        webSocketOptions.mergeWebSocketFrame(mergeWebSocketFrame);
        return this;
    }

    public int maxWebSocketFrameSize() {
        return webSocketOptions.maxWebSocketFrameSize();
    }

    public XHttpServerOptions maxWebSocketFrameSize(int maxWebSocketFrameSize) {
        webSocketOptions.maxWebSocketFrameSize(maxWebSocketFrameSize);
        return this;
    }

    public int maxWebSocketMessageSize() {
        return webSocketOptions.maxWebSocketMessageSize();
    }

    public XHttpServerOptions maxWebSocketMessageSize(int maxWebSocketMessageSize) {
        webSocketOptions.maxWebSocketMessageSize(maxWebSocketMessageSize);
        return this;
    }

    public boolean enableHttp2() {
        return enableHttp2;
    }

    public XHttpServerOptions enableHttp2(boolean enableHttp2) {
        this.enableHttp2 = enableHttp2;
        return this;
    }

    public InetSocketAddress localAddress() {
        return tcpServerOptions.localAddress();
    }

    public XHttpServerOptions localAddress(InetSocketAddress localAddress) {
        tcpServerOptions.localAddress(localAddress);
        return this;
    }

    public int backlog() {
        return tcpServerOptions.backlog();
    }

    public XHttpServerOptions backlog(int backlog) {
        this.tcpServerOptions.backlog(backlog);
        return this;
    }

    public TLS tls() {
        return tcpServerOptions.tls();
    }

    public XHttpServerOptions tls(TLS tls) {
        this.tcpServerOptions.tls(tls);
        return this;
    }

    public XHttpServerOptions port(int port) {
        tcpServerOptions.port(port);
        return this;
    }

    public enum TCPServerType {
        CLASSIC,
        NIO
    }

}
