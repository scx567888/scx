package cool.scx.net.tls;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.security.KeyStore;

import static cool.scx.net.tls.TLSHelper.*;

/**
 * TLS 配置
 */
public class TLS {

    private final Path path;
    private final String password;
    private final KeyStore keyStore;
    private final KeyManagerFactory KeyManagerFactory;
    private final TrustManagerFactory trustManagerFactory;
    private final SSLContext sslContext;
    private final SSLServerSocketFactory serverSocketFactory;
    private final SSLSocketFactory socketFactory;
    private boolean enabled;

    public TLS(Path path, String password) {
        this.enabled = true;
        this.path = path;
        this.password = password;
        this.keyStore = createKeyStore(path, password);
        this.KeyManagerFactory = createKeyManagerFactory(keyStore, password);
        this.trustManagerFactory = createTrustManagerFactory(keyStore);
        this.sslContext = createSSLContext(KeyManagerFactory, trustManagerFactory);
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

    public ServerSocket createServerSocket() {
        try {
            return serverSocketFactory.createServerSocket();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Socket createSocket() {
        try {
            return socketFactory.createSocket();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
