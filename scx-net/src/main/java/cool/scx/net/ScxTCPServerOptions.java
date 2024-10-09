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
        this.port = port;
        return this;
    }

    public TLS tls() {
        return tls;
    }

    public ScxTCPServerOptions tls(TLS tls) {
        this.tls = tls;
        return this;
    }

}
