package cool.scx.http.usagi;

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
    private TCPServerType tcpServerType; // TCP 服务器类型

    private int maxRequestLineSize;// 最大请求行大小
    private int maxHeaderSize;// 最大请求头大小
    private int maxPayloadSize;// 最大请求体大小

    private boolean mergeWebSocketFrame;//是否合并 WebSocket 帧
    private int maxWebSocketFrameSize; // 最大单个 WebSocket 帧长度
    private int maxWebSocketMessageSize;// 最大 WebSocket 消息长度 (可能由多个帧合并)

    private boolean enableHttp2; // 是否开启 Http2

    public UsagiHttpServerOptions() {
        //默认不自动握手
        this.tcpServerOptions = new ScxTCPServerOptions().autoUpgradeToTLS(true).autoHandshake(false);
        this.tcpServerType = TCPServerType.CLASSIC; // 默认 使用 CLASSIC 实现
        this.maxRequestLineSize = 1024 * 64; // 默认 64 KB
        this.maxHeaderSize = 1024 * 128; // 默认 128 KB
        this.maxPayloadSize = 1024 * 1024 * 16; // 默认 16 MB
        this.mergeWebSocketFrame = true; // 默认 合并 websocket 帧
        this.maxWebSocketFrameSize = 1024 * 1024 * 16; // 默认 16 MB
        this.maxWebSocketMessageSize = 1024 * 1024 * 16; // 默认 16 MB
        this.enableHttp2 = false;//默认不启用 http2
    }

    public UsagiHttpServerOptions(UsagiHttpServerOptions oldOptions) {
        //默认不自动握手
        this.tcpServerOptions = new ScxTCPServerOptions(oldOptions.tcpServerOptions()).autoUpgradeToTLS(true).autoHandshake(false);
        tcpServerType(oldOptions.tcpServerType());
        maxRequestLineSize(oldOptions.maxRequestLineSize());
        maxHeaderSize(oldOptions.maxHeaderSize());
        maxPayloadSize(oldOptions.maxPayloadSize());
        mergeWebSocketFrame(oldOptions.mergeWebSocketFrame());
        maxWebSocketFrameSize(oldOptions.maxWebSocketFrameSize());
        maxWebSocketMessageSize(oldOptions.maxWebSocketMessageSize());
        enableHttp2(oldOptions.enableHttp2());
    }

    //不允许外界访问
    ScxTCPServerOptions tcpServerOptions() {
        return tcpServerOptions;
    }

    public TCPServerType tcpServerType() {
        return tcpServerType;
    }

    public UsagiHttpServerOptions tcpServerType(TCPServerType tcpServerType) {
        this.tcpServerType = tcpServerType;
        return this;
    }

    public int maxRequestLineSize() {
        return maxRequestLineSize;
    }

    public UsagiHttpServerOptions maxRequestLineSize(int maxRequestLineSize) {
        this.maxRequestLineSize = maxRequestLineSize;
        return this;
    }

    public int maxHeaderSize() {
        return maxHeaderSize;
    }

    public UsagiHttpServerOptions maxHeaderSize(int maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
        return this;
    }

    public int maxPayloadSize() {
        return maxPayloadSize;
    }

    public UsagiHttpServerOptions maxPayloadSize(int maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
        return this;
    }


    public boolean mergeWebSocketFrame() {
        return mergeWebSocketFrame;
    }

    public UsagiHttpServerOptions mergeWebSocketFrame(boolean mergeWebSocketFrame) {
        this.mergeWebSocketFrame = mergeWebSocketFrame;
        return this;
    }

    public int maxWebSocketFrameSize() {
        return maxWebSocketFrameSize;
    }

    public UsagiHttpServerOptions maxWebSocketFrameSize(int maxWebSocketFrameSize) {
        this.maxWebSocketFrameSize = maxWebSocketFrameSize;
        return this;
    }

    public int maxWebSocketMessageSize() {
        return maxWebSocketMessageSize;
    }

    public UsagiHttpServerOptions maxWebSocketMessageSize(int maxWebSocketMessageSize) {
        this.maxWebSocketMessageSize = maxWebSocketMessageSize;
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
