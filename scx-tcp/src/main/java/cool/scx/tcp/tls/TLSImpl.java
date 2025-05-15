package cool.scx.tcp.tls;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

/// 默认实现
///
/// @author scx567888
/// @version 0.0.1
class TLSImpl implements TLS {

    private final SSLContext sslContext;
    private final SSLServerSocketFactory serverSocketFactory;
    private final SSLSocketFactory socketFactory;
    private boolean enabled;

    TLSImpl(SSLContext sslContext) {
        this.enabled = true;
        this.sslContext = sslContext;
        this.serverSocketFactory = this.sslContext.getServerSocketFactory();
        this.socketFactory = this.sslContext.getSocketFactory();
    }

    public boolean enabled() {
        return enabled;
    }

    public TLS enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public SSLContext sslContext() {
        return sslContext;
    }

    public SSLServerSocketFactory serverSocketFactory() {
        return serverSocketFactory;
    }

    public SSLSocketFactory socketFactory() {
        return socketFactory;
    }

}
