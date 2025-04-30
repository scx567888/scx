package cool.scx.tcp;

import cool.scx.tcp.tls.TLS;

/// TCPClientOptions
///
/// @author scx567888
/// @version 0.0.1
public class TCPClientOptions {

    private TLS tls;
    private boolean autoUpgradeToTLS;
    private boolean autoHandshake;

    public TCPClientOptions() {
        this.tls = null; // 默认没有 tls
        this.autoUpgradeToTLS = true; // 自动升级到 TLS
        this.autoHandshake = true; // 自动握手
    }

    public TCPClientOptions(TCPClientOptions oldOptions) {
        tls(oldOptions.tls());
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
