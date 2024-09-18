package cool.scx.http;

import cool.scx.http.tls.TLS;

/**
 * ScxHttpServerOptions
 */
public class ScxHttpServerOptions {

    private int port;
    private TLS tls;
    private long maxPayloadSize;

    public ScxHttpServerOptions() {
        this.port = 0;
        this.tls = null;
        this.maxPayloadSize = -1;
    }

    public int getPort() {
        return port;
    }

    public ScxHttpServerOptions setPort(int port) {
        if (port > 65535) {
            throw new IllegalArgumentException("port must be <= 65535");
        } else {
            this.port = port;
            return this;
        }
    }

    public TLS getTLS() {
        return tls;
    }

    public void setTLS(TLS tls) {
        this.tls = tls;
    }

    public void setMaxPayloadSize(long maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
    }

    public long getMaxPayloadSize() {
        return this.maxPayloadSize;
    }

}
