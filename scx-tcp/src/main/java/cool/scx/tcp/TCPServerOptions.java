package cool.scx.tcp;

import cool.scx.tcp.tls.TLS;

/// ScxTCPServerOptions
///
/// @author scx567888
/// @version 0.0.1
public class TCPServerOptions {

    private int backlog;
    private TLS tls;
    private boolean autoUpgradeToTLS;
    private boolean autoHandshake;

    public TCPServerOptions() {
        this.backlog = 128; // 默认背压大小 128
        this.tls = null; // 默认没有 tls
        this.autoUpgradeToTLS = true; // 自动升级到 TLS
        this.autoHandshake = true; // 自动握手
    }

    public TCPServerOptions(TCPServerOptions oldOptions) {
        backlog(oldOptions.backlog());
        tls(oldOptions.tls());
        autoUpgradeToTLS(oldOptions.autoUpgradeToTLS());
        autoHandshake(oldOptions.autoHandshake());
    }

    public int backlog() {
        return backlog;
    }

    public TCPServerOptions backlog(int backlog) {
        this.backlog = backlog;
        return this;
    }

    public TLS tls() {
        return tls;
    }

    public TCPServerOptions tls(TLS tls) {
        this.tls = tls;
        return this;
    }

    public boolean autoUpgradeToTLS() {
        return autoUpgradeToTLS;
    }

    public TCPServerOptions autoUpgradeToTLS(boolean autoUpgradeToTLS) {
        this.autoUpgradeToTLS = autoUpgradeToTLS;
        return this;
    }

    public boolean autoHandshake() {
        return autoHandshake;
    }

    public TCPServerOptions autoHandshake(boolean autoHandshake) {
        this.autoHandshake = autoHandshake;
        return this;
    }

}

