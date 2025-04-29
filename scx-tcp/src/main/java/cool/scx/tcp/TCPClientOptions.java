package cool.scx.tcp;

import cool.scx.tcp.proxy.Proxy;
import cool.scx.tcp.tls.TLS;

/// ScxTCPClientOptions
///
/// @author scx567888
/// @version 0.0.1
public class TCPClientOptions {

    private Proxy proxy;
    private TLS tls;
    private boolean autoUpgradeToTLS;
    private boolean autoHandshake;

    public TCPClientOptions() {
        this.tls = null;
        this.proxy = null;
        this.autoUpgradeToTLS = true;
        this.autoHandshake = true;
    }

    public TCPClientOptions(TCPClientOptions oldOptions) {
        tls(oldOptions.tls());
        proxy(oldOptions.proxy());
        autoUpgradeToTLS(oldOptions.autoUpgradeToTLS());
        autoHandshake(oldOptions.autoHandshake());
    }

    public TLS tls() {
        return tls;
    }

    public TCPClientOptions tls(TLS tls) {
        this.tls = tls;
        return this;
    }

    public Proxy proxy() {
        return this.proxy;
    }

    public TCPClientOptions proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public boolean autoUpgradeToTLS() {
        return autoUpgradeToTLS;
    }

    public TCPClientOptions autoUpgradeToTLS(boolean autoUpgradeToTLS) {
        this.autoUpgradeToTLS = autoUpgradeToTLS;
        return this;
    }

    public boolean autoHandshake() {
        return autoHandshake;
    }

    public TCPClientOptions autoHandshake(boolean autoHandshake) {
        this.autoHandshake = autoHandshake;
        return this;
    }

}
