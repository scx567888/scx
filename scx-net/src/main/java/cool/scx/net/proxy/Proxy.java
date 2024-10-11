package cool.scx.net.proxy;

import java.net.InetSocketAddress;
import java.net.Proxy.Type;
import java.net.SocketAddress;

import static java.net.Proxy.Type.SOCKS;

public class Proxy {

    private final SocketAddress proxyAddress;
    private final java.net.Proxy proxy;
    private boolean enabled;

    public Proxy(SocketAddress proxyAddress, Type type) {
        this.enabled = true;
        this.proxyAddress = proxyAddress;
        this.proxy = new java.net.Proxy(type, proxyAddress);
    }

    public Proxy(SocketAddress proxyAddress) {
        this(proxyAddress, SOCKS);
    }

    public Proxy(int port, Type type) {
        this(new InetSocketAddress(port), type);
    }

    public Proxy(int port) {
        this(new InetSocketAddress(port));
    }

    public boolean enabled() {
        return enabled;
    }

    public Proxy enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public SocketAddress proxyAddress() {
        return proxyAddress;
    }

    public java.net.Proxy proxy() {
        return proxy;
    }

}
