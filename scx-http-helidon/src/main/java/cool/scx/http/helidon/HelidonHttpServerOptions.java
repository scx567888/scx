package cool.scx.http.helidon;

import io.helidon.common.tls.Tls;

/**
 * ScxHttpServerOptions
 */
public class HelidonHttpServerOptions {

    private int port;
    private Tls tls;
    private long maxPayloadSize;
    private int bodyBufferSize;

    public HelidonHttpServerOptions() {
        this.port = 0;
        this.tls = null;
        this.maxPayloadSize = -1;
        this.bodyBufferSize = 65536;
    }

    public int port() {
        return port;
    }

    public HelidonHttpServerOptions port(int port) {
        if (port > 65535) {
            throw new IllegalArgumentException("port must be <= 65535");
        } else {
            this.port = port;
            return this;
        }
    }

    public Tls tls() {
        return tls;
    }

    public HelidonHttpServerOptions tls(Tls tls) {
        this.tls = tls;
        return this;
    }

    public long maxPayloadSize() {
        return this.maxPayloadSize;
    }

    public HelidonHttpServerOptions maxPayloadSize(long maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
        return this;
    }

    public int bodyBufferSize() {
        return bodyBufferSize;
    }

    public HelidonHttpServerOptions bodyBufferSize(int bodyBufferSize) {
        this.bodyBufferSize = bodyBufferSize;
        return this;
    }

}
