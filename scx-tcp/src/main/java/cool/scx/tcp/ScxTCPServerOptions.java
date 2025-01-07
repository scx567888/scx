package cool.scx.tcp;

import cool.scx.tcp.tls.TLS;

import java.net.InetSocketAddress;


/**
 * ScxTCPServerOptions
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ScxTCPServerOptions {

    private InetSocketAddress localAddress;
    private int backlog;
    private TLS tls;
    private boolean autoUpgradeToTLS;
    private boolean autoHandshake;

    public ScxTCPServerOptions() {
        this.localAddress = new InetSocketAddress(0); // 默认随机端口号
        this.backlog = 0; // 默认采用实现的默认背压
        this.tls = null; // 默认没有 tls
        this.autoUpgradeToTLS = true; // 自动升级到 TLS
        this.autoHandshake = true; // 自动握手
    }

    public ScxTCPServerOptions(ScxTCPServerOptions oldOptions) {
        localAddress(oldOptions.localAddress());
        backlog(oldOptions.backlog());
        tls(oldOptions.tls());
        autoUpgradeToTLS(oldOptions.autoUpgradeToTLS());
        autoHandshake(oldOptions.autoHandshake());
    }

    public InetSocketAddress localAddress() {
        return localAddress;
    }

    public ScxTCPServerOptions localAddress(InetSocketAddress localAddress) {
        this.localAddress = localAddress;
        return this;
    }

    public int backlog() {
        return backlog;
    }

    public ScxTCPServerOptions backlog(int backlog) {
        this.backlog = backlog;
        return this;
    }

    public TLS tls() {
        return tls;
    }

    public ScxTCPServerOptions tls(TLS tls) {
        this.tls = tls;
        return this;
    }

    public boolean autoUpgradeToTLS() {
        return autoUpgradeToTLS;
    }

    public ScxTCPServerOptions autoUpgradeToTLS(boolean autoUpgradeToTLS) {
        this.autoUpgradeToTLS = autoUpgradeToTLS;
        return this;
    }

    public boolean autoHandshake() {
        return autoHandshake;
    }

    public ScxTCPServerOptions autoHandshake(boolean autoHandshake) {
        this.autoHandshake = autoHandshake;
        return this;
    }

    //简易方法
    public ScxTCPServerOptions port(int port) {
        return localAddress(new InetSocketAddress(port));
    }

}
