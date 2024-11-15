package cool.scx.http.peach;


import cool.scx.net.ScxTCPServerOptions;
import cool.scx.net.tls.TLS;

/**
 * ScxHttpServerOptions
 */
public class PeachHttpServerOptions extends ScxTCPServerOptions {
    
    private long maxPayloadSize;
    private int bodyBufferSize;

    public PeachHttpServerOptions() {
        this.maxPayloadSize = -1;
        this.bodyBufferSize = 65536;
    }

    @Override
    public PeachHttpServerOptions port(int port) {
        super.port(port);
        return this;
    }

    @Override
    public PeachHttpServerOptions tls(TLS tls) {
        super.tls(tls);
        return this;
    }

    public long maxPayloadSize() {
        return this.maxPayloadSize;
    }

    public PeachHttpServerOptions maxPayloadSize(long maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
        return this;
    }

    public int bodyBufferSize() {
        return bodyBufferSize;
    }

    public PeachHttpServerOptions bodyBufferSize(int bodyBufferSize) {
        this.bodyBufferSize = bodyBufferSize;
        return this;
    }

}
