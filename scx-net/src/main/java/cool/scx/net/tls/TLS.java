package cool.scx.net.tls;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

import static cool.scx.net.tls.TLSHelper.*;

/**
 * TLS 配置
 */
public class TLS {

    private final SSLContext sslContext;
    private final SSLServerSocketFactory serverSocketFactory;
    private final SSLSocketFactory socketFactory;
    private boolean enabled;

    public TLS(Path path, String password) {
        this.enabled = true;
        var keyStore = createKeyStore(path, password);
        var KeyManagerFactory = createKeyManagerFactory(keyStore, password);
        var trustManagerFactory = createTrustManagerFactory(keyStore);
        this.sslContext = createSSLContext(KeyManagerFactory, trustManagerFactory);
        this.serverSocketFactory = sslContext.getServerSocketFactory();
        this.socketFactory = sslContext.getSocketFactory();
    }

    public TLS(SSLContext sslContext) {
        this.enabled = true;
        this.sslContext = sslContext;
        this.serverSocketFactory = sslContext.getServerSocketFactory();
        this.socketFactory = sslContext.getSocketFactory();
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

    public ServerSocket createServerSocket() throws IOException {
        return serverSocketFactory.createServerSocket();
    }

    public Socket createSocket() throws IOException {
        return socketFactory.createSocket();
    }

}
