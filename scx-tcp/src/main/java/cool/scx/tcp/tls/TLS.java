package cool.scx.tcp.tls;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.nio.file.Path;

import static cool.scx.tcp.tls.TLSHelper.*;

/// TLS 配置
///
/// @author scx567888
/// @version 0.0.1
public interface TLS {

    static TLS of(Path path, String password) {
        return new TLSImpl(createSSLContext(path, password));
    }

    static TLS of(SSLContext sslContext) {
        return new TLSImpl(sslContext);
    }

    static TLS ofPem(Path pemPath, Path keyPath) {
        return new TLSImpl(createSSLContextFromPem(pemPath, keyPath));
    }

    static TLS ofPem(String pemContent, String keyContent) {
        return new TLSImpl(createSSLContextFromPem(pemContent, keyContent));
    }

    static TLS ofDefault() {
        return new TLSImpl(createDefaultSSLContext());
    }

    static TLS ofTrustAny() {
        return new TLSImpl(createTrustAnySSLContext());
    }

    SSLContext sslContext();

    SSLServerSocketFactory serverSocketFactory();

    SSLSocketFactory socketFactory();

}
