package cool.scx.net;

import cool.scx.net.proxy.Proxy;
import cool.scx.net.tls.TLS;

public class ScxTCPClientOptions {

    private TLS tls;
    private Proxy proxy;

    public ScxTCPClientOptions() {
        this.tls = null;
        this.proxy = null;
    }

    public TLS tls() {
        return tls;
    }

    public ScxTCPClientOptions tls(TLS tls) {
        this.tls = tls;
        return this;
    }

    public Proxy proxy() {
        return this.proxy;
    }

    public ScxTCPClientOptions proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

}
