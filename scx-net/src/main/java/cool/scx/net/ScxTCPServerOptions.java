package cool.scx.net;

import cool.scx.net.tls.TLS;

public class ScxTCPServerOptions {

    private int port;

    private TLS tls;

    public ScxTCPServerOptions() {
        this.port = 0;
        this.tls = null;
    }

    public int port() {
        return port;
    }

    public ScxTCPServerOptions port(int port) {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("port must be between 0 and 65535");
        } else {
            this.port = port;
            return this;
        }
    }

    public TLS tls() {
        return tls;
    }

    public ScxTCPServerOptions tls(TLS tls) {
        this.tls = tls;
        return this;
    }

}
