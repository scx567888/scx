package cool.scx.http.x.proxy;

import java.net.SocketAddress;

class ProxyImpl implements Proxy {

    private final SocketAddress proxyAddress;
    private boolean enabled;

    public ProxyImpl(SocketAddress proxyAddress) {
        this.enabled = true;
        this.proxyAddress = proxyAddress;
    }

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public Proxy enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public SocketAddress proxyAddress() {
        return this.proxyAddress;
    }

}
