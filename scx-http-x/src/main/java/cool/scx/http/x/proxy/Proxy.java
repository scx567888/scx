package cool.scx.http.x.proxy;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/// Http 代理
///
/// @author scx567888
/// @version 0.0.1
public interface Proxy {

    static Proxy of(InetSocketAddress address) {
        return new ProxyImpl(address);
    }

    static Proxy of(String host, int port) {
        return new ProxyImpl(new InetSocketAddress(host, port));
    }

    static Proxy of(int port) {
        return new ProxyImpl(new InetSocketAddress(port));
    }

    boolean enabled();

    Proxy enabled(boolean enabled);

    SocketAddress proxyAddress();

}
