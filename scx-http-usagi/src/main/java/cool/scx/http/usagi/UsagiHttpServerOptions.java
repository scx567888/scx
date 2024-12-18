package cool.scx.http.usagi;

import cool.scx.tcp.ScxTCPServerOptions;
import cool.scx.tcp.tls.TLS;

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

    public UsagiHttpServerOptions() {
        this.maxRequestLineSize = 1024 * 64; // 默认 64 KB
        this.maxHeaderSize = 1024 * 128; // 默认 128 KB
        this.maxPayloadSize = 1024 * 1024 * 16; // 默认 16 MB
        this.maxWebsocketFrameSize = 1024 * 1024 * 16; // 默认 16 MB
    }

    public UsagiHttpServerOptions(UsagiHttpServerOptions oldOptions) {
        port(oldOptions.port());
        tls(oldOptions.tls());
        maxRequestLineSize(oldOptions.maxRequestLineSize());
        maxHeaderSize(oldOptions.maxHeaderSize());
        maxPayloadSize(oldOptions.maxPayloadSize());
        maxWebsocketFrameSize(oldOptions.maxWebsocketFrameSize());
    }

    @Override
    public UsagiHttpServerOptions port(int port) {
        super.port(port);
        return this;
    }

    @Override
    public UsagiHttpServerOptions tls(TLS tls) {
        super.tls(tls);
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

    public int maxWebsocketFrameSize() {
        return maxWebsocketFrameSize;
    }

    public UsagiHttpServerOptions maxWebsocketFrameSize(int maxWebsocketFrameSize) {
        this.maxWebsocketFrameSize = maxWebsocketFrameSize;
        return this;
    }

}
