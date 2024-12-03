package cool.scx.http.usagi;


import cool.scx.tcp.ScxTCPServerOptions;
import cool.scx.tcp.tls.TLS;

/**
 * ScxHttpServerOptions
 */
public class UsagiHttpServerOptions extends ScxTCPServerOptions {

    private long maxPayloadSize;
    private int bodyBufferSize;

    public UsagiHttpServerOptions() {
        this.maxPayloadSize = -1;
        this.bodyBufferSize = 65536;
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

    public long maxPayloadSize() {
        return this.maxPayloadSize;
    }

    public UsagiHttpServerOptions maxPayloadSize(long maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
        return this;
    }

    public int bodyBufferSize() {
        return bodyBufferSize;
    }

    public UsagiHttpServerOptions bodyBufferSize(int bodyBufferSize) {
        this.bodyBufferSize = bodyBufferSize;
        return this;
    }

}
