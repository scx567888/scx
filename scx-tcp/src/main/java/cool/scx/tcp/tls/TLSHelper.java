package cool.scx.tcp.tls;

import javax.net.ssl.*;
import java.io.IOException;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/// TLSHelper
///
/// @author scx567888
/// @version 0.0.1
public final class TLSHelper {

    public static SSLContext createSSLContext(Path path, String password) {
        try {
            var keyStore = createKeyStore(path, password);
            return createSSLContext(keyStore, password);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static KeyStore createKeyStore(Path path, String password) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        // 证书存储器
        return KeyStore.getInstance(path.toFile(), password.toCharArray());
    }

    private static SSLContext createSSLContext(KeyStore keyStore, String password) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        var KeyManagerFactory = createKeyManagerFactory(keyStore, password);
        var trustManagerFactory = createTrustManagerFactory(keyStore);
        return createSSLContext(KeyManagerFactory, trustManagerFactory);
    }

    private static KeyManagerFactory createKeyManagerFactory(KeyStore keyStore, String password) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        // 初始化密钥管理器工厂
        var keyManagerFactory = KeyManagerFactory.getInstance("PKIX");
        keyManagerFactory.init(keyStore, password.toCharArray());
        return keyManagerFactory;
    }

    private static TrustManagerFactory createTrustManagerFactory(KeyStore keyStore) throws NoSuchAlgorithmException, KeyStoreException {
        // 初始化 TrustManagerFactory
        var trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
        trustManagerFactory.init(keyStore);
        return trustManagerFactory;
    }

    private static SSLContext createSSLContext(KeyManagerFactory keyManagerFactory, TrustManagerFactory trustManagerFactory) throws NoSuchAlgorithmException, KeyManagementException {
        var sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        return sslContext;
    }

    // 获取系统默认证书并返回 TLS 对象（用于客户端） 
    public static SSLContext createDefaultSSLContext() {
        try {
            var tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);
            var sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new IllegalStateException("Failed to initialize default TLS configuration", e);
        }
    }

    public static SSLContext createTrustAnySSLContext() {
        // 创建自定义 TrustManager, 忽略证书验证（仅用于测试环境）
        var trustAllCerts = new TrustManager[]{new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                // 此处忽略客户端证书验证逻辑
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                // 此处忽略服务器证书验证逻辑
            }
        }};

        try {
            var sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null);
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new IllegalStateException(e);
        }
    }

}
