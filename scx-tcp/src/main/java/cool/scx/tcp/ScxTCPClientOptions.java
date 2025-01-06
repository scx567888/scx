package cool.scx.tcp;

import cool.scx.tcp.proxy.Proxy;
import cool.scx.tcp.tls.TLS;


/**
 * ScxTCPClientOptions
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ScxTCPClientOptions {

    private TLS tls;
    private Proxy proxy;

    public ScxTCPClientOptions() {
        this.tls = null;
        this.proxy = null;
    }

    public ScxTCPClientOptions(ScxTCPClientOptions oldOptions) {
        tls(oldOptions.tls());
        proxy(oldOptions.proxy());
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
