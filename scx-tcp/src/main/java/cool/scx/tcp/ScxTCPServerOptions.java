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

    public ScxTCPServerOptions() {
        this.localAddress = new InetSocketAddress(0); // 默认随机端口号
        this.backlog = 0; // 默认采用实现的默认背压
        this.tls = null; // 默认没有 tls
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
    
    //简易方法
    public ScxTCPServerOptions port(int port) {
        return localAddress(new InetSocketAddress(port));
    }

}
