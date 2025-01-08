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

    private final ScxTCPServerOptions tcpServerOptions;
    private int maxRequestLineSize;
    private int maxHeaderSize;
    private int maxPayloadSize;
    private int maxWebsocketFrameSize;
    private TCPServerType tcpServerType;
    private boolean enableHttp2;

    public UsagiHttpServerOptions() {
        //默认不自动握手
        this.tcpServerOptions = new ScxTCPServerOptions().autoUpgradeToTLS(true).autoHandshake(false);
        this.maxRequestLineSize = 1024 * 64; // 默认 64 KB
        this.maxHeaderSize = 1024 * 128; // 默认 128 KB
        this.maxPayloadSize = 1024 * 1024 * 16; // 默认 16 MB
        this.maxWebsocketFrameSize = 1024 * 1024 * 16; // 默认 16 MB
        this.tcpServerType = TCPServerType.CLASSIC; // 默认 使用 CLASSIC 实现
        this.enableHttp2 = false;//默认不启用 http2
    }

    public UsagiHttpServerOptions(UsagiHttpServerOptions oldOptions) {
        //默认不自动握手
        this.tcpServerOptions = new ScxTCPServerOptions(oldOptions.tcpServerOptions).autoUpgradeToTLS(true).autoHandshake(false);
        maxRequestLineSize(oldOptions.maxRequestLineSize());
        maxHeaderSize(oldOptions.maxHeaderSize());
        maxPayloadSize(oldOptions.maxPayloadSize());
        maxWebsocketFrameSize(oldOptions.maxWebsocketFrameSize());
        tcpServerType(oldOptions.tcpServerType());
        enableHttp2(oldOptions.enableHttp2());
    }
    
    //不允许外界访问
    ScxTCPServerOptions tcpServerOptions() {
        return tcpServerOptions;
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

    public int maxWebsocketFrameSize() {
        return maxWebsocketFrameSize;
    }

    public UsagiHttpServerOptions maxWebsocketFrameSize(int maxWebsocketFrameSize) {
        this.maxWebsocketFrameSize = maxWebsocketFrameSize;
        return this;
    }

    public TCPServerType tcpServerType() {
        return tcpServerType;
    }

    public UsagiHttpServerOptions tcpServerType(TCPServerType tcpServerType) {
        this.tcpServerType = tcpServerType;
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
        this.tcpServerOptions.tls(tls) ;
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
