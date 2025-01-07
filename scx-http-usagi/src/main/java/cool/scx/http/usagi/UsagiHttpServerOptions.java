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
public class UsagiHttpServerOptions extends ScxTCPServerOptions {

    private int maxRequestLineSize;
    private int maxHeaderSize;
    private int maxPayloadSize;
    private int maxWebsocketFrameSize;
    private TCPServerType tcpServerType;

    public UsagiHttpServerOptions() {
        this.maxRequestLineSize = 1024 * 64; // 默认 64 KB
        this.maxHeaderSize = 1024 * 128; // 默认 128 KB
        this.maxPayloadSize = 1024 * 1024 * 16; // 默认 16 MB
        this.maxWebsocketFrameSize = 1024 * 1024 * 16; // 默认 16 MB
        this.tcpServerType = TCPServerType.CLASSIC; // 默认 使用 CLASSIC 实现
    }

    public UsagiHttpServerOptions(UsagiHttpServerOptions oldOptions) {
        super(oldOptions);
        maxRequestLineSize(oldOptions.maxRequestLineSize());
        maxHeaderSize(oldOptions.maxHeaderSize());
        maxPayloadSize(oldOptions.maxPayloadSize());
        maxWebsocketFrameSize(oldOptions.maxWebsocketFrameSize());
        tcpServerType(oldOptions.tcpServerType());
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

    @Override
    public UsagiHttpServerOptions localAddress(InetSocketAddress localAddress) {
        super.localAddress(localAddress);
        return this;
    }

    @Override
    public UsagiHttpServerOptions backlog(int backlog) {
        super.backlog(backlog);
        return this;
    }

    @Override
    public UsagiHttpServerOptions tls(TLS tls) {
        super.tls(tls);
        return this;
    }

    @Override
    public UsagiHttpServerOptions port(int port) {
        super.port(port);
        return this;
    }

    @Override
    public boolean autoUpgradeToTLS() {
        //永远自动升级到 TLS
        return true;
    }

    @Override
    public ScxTCPServerOptions autoUpgradeToTLS(boolean autoUpgradeToTLS) {
        //什么都不做
        return this;
    }

    @Override
    public boolean autoHandshake() {
        //永远不自动握手
        return false;
    }

    @Override
    public ScxTCPServerOptions autoHandshake(boolean autoHandshake) {
        //什么都不做
        return this;
    }

    public enum TCPServerType {
        CLASSIC,
        NIO
    }

}
