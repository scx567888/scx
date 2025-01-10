package cool.scx.http.usagi;

import cool.scx.http.usagi.http1x.Http1xConnectionOptions;
import cool.scx.http.usagi.web_socket.WebSocketOptions;
import cool.scx.tcp.ScxTCPServerOptions;
import cool.scx.tcp.tls.TLS;

import java.net.InetSocketAddress;

/**
 * Http 服务器配置
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiHttpServerOptions {

    private final ScxTCPServerOptions tcpServerOptions; // TCP 服务器配置
    private final Http1xConnectionOptions http1xConnectionOptions;// Http1 配置
    private final WebSocketOptions webSocketOptions;// WebSocket 配置
    private TCPServerType tcpServerType; // TCP 服务器类型
    private boolean enableHttp2; // 是否开启 Http2

    public UsagiHttpServerOptions() {
        //默认不自动握手
        this.tcpServerOptions = new ScxTCPServerOptions().autoUpgradeToTLS(true).autoHandshake(false);
        this.http1xConnectionOptions = new Http1xConnectionOptions();
        this.webSocketOptions = new WebSocketOptions();
        this.tcpServerType = TCPServerType.CLASSIC; // 默认 使用 CLASSIC 实现
        this.enableHttp2 = false;//默认不启用 http2
    }

    public UsagiHttpServerOptions(UsagiHttpServerOptions oldOptions) {
        //默认不自动握手
        this.tcpServerOptions = new ScxTCPServerOptions(oldOptions.tcpServerOptions()).autoUpgradeToTLS(true).autoHandshake(false);
        this.http1xConnectionOptions = new Http1xConnectionOptions(oldOptions.http1xConnectionOptions());
        this.webSocketOptions = new WebSocketOptions(oldOptions.webSocketOptions());
        tcpServerType(oldOptions.tcpServerType());
        enableHttp2(oldOptions.enableHttp2());
    }

    //因为涉及到一些底层实现, 所以不允许外界访问
    ScxTCPServerOptions tcpServerOptions() {
        return tcpServerOptions;
    }

    public Http1xConnectionOptions http1xConnectionOptions() {
        return http1xConnectionOptions;
    }

    public WebSocketOptions webSocketOptions() {
        return webSocketOptions;
    }

    public TCPServerType tcpServerType() {
        return tcpServerType;
    }

    public UsagiHttpServerOptions tcpServerType(TCPServerType tcpServerType) {
        this.tcpServerType = tcpServerType;
        return this;
    }

    public int maxRequestLineSize() {
        return http1xConnectionOptions.maxRequestLineSize();
    }

    public UsagiHttpServerOptions maxRequestLineSize(int maxRequestLineSize) {
        http1xConnectionOptions.maxRequestLineSize(maxRequestLineSize);
        return this;
    }

    public int maxHeaderSize() {
        return http1xConnectionOptions.maxHeaderSize();
    }

    public UsagiHttpServerOptions maxHeaderSize(int maxHeaderSize) {
        http1xConnectionOptions.maxHeaderSize(maxHeaderSize);
        return this;
    }

    public int maxPayloadSize() {
        return http1xConnectionOptions.maxPayloadSize();
    }

    public UsagiHttpServerOptions maxPayloadSize(int maxPayloadSize) {
        http1xConnectionOptions.maxPayloadSize(maxPayloadSize);
        return this;
    }

    public boolean mergeWebSocketFrame() {
        return webSocketOptions.mergeWebSocketFrame();
    }

    public UsagiHttpServerOptions mergeWebSocketFrame(boolean mergeWebSocketFrame) {
        webSocketOptions.mergeWebSocketFrame(mergeWebSocketFrame);
        return this;
    }

    public int maxWebSocketFrameSize() {
        return webSocketOptions.maxWebSocketFrameSize();
    }

    public UsagiHttpServerOptions maxWebSocketFrameSize(int maxWebSocketFrameSize) {
        webSocketOptions.maxWebSocketFrameSize(maxWebSocketFrameSize);
        return this;
    }

    public int maxWebSocketMessageSize() {
        return webSocketOptions.maxWebSocketMessageSize();
    }

    public UsagiHttpServerOptions maxWebSocketMessageSize(int maxWebSocketMessageSize) {
        webSocketOptions.maxWebSocketMessageSize(maxWebSocketMessageSize);
        return this;
    }

    public boolean enableHttp2() {
        return enableHttp2;
    }

    public UsagiHttpServerOptions enableHttp2(boolean enableHttp2) {
        this.enableHttp2 = enableHttp2;
        return this;
    }

    public InetSocketAddress localAddress() {
        return tcpServerOptions.localAddress();
    }

    public UsagiHttpServerOptions localAddress(InetSocketAddress localAddress) {
        tcpServerOptions.localAddress(localAddress);
        return this;
    }

    public int backlog() {
        return tcpServerOptions.backlog();
    }

    public UsagiHttpServerOptions backlog(int backlog) {
        this.tcpServerOptions.backlog(backlog);
        return this;
    }

    public TLS tls() {
        return tcpServerOptions.tls();
    }

    public UsagiHttpServerOptions tls(TLS tls) {
        this.tcpServerOptions.tls(tls);
        return this;
    }

    public UsagiHttpServerOptions port(int port) {
        tcpServerOptions.port(port);
        return this;
    }

    public enum TCPServerType {
        CLASSIC,
        NIO
    }

}
