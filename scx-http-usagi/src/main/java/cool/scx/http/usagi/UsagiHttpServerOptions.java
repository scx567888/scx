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

    public void maxRequestLineSize(int maxRequestLineSize) {
        this.maxRequestLineSize = maxRequestLineSize;
    }

    public int maxHeaderSize() {
        return maxHeaderSize;
    }

    public void maxHeaderSize(int maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
    }

    public int maxPayloadSize() {
        return maxPayloadSize;
    }

    public void maxPayloadSize(int maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
    }

    public int maxWebsocketFrameSize() {
        return maxWebsocketFrameSize;
    }

    public void maxWebsocketFrameSize(int maxWebsocketFrameSize) {
        this.maxWebsocketFrameSize = maxWebsocketFrameSize;
    }

}
