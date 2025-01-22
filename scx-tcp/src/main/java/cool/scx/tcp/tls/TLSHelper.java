package cool.scx.tcp.tls;

import javax.net.ssl.*;
import java.io.IOException;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


/**
 * TLSHelper
 *
 * @author scx567888
 * @version 0.0.1
 */
public class TLSHelper {

    public static KeyStore createKeyStore(Path path, String password) {
        // 证书存储器
        try {
            return KeyStore.getInstance(path.toFile(), password.toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new IllegalArgumentException("failed to create keystore ", e);
        }
    }

    public static KeyManagerFactory createKeyManagerFactory(KeyStore keyStore, String password) {
        try {
            // 初始化密钥管理器工厂
            var keyManagerFactory = KeyManagerFactory.getInstance("PKIX");
            keyManagerFactory.init(keyStore, password.toCharArray());
            return keyManagerFactory;
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new IllegalArgumentException("failed to create key manager factory");
        }
    }

    public static TrustManagerFactory createTrustManagerFactory(KeyStore keyStore) {
        try {
            // 初始化 TrustManagerFactory
            var trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
            trustManagerFactory.init(keyStore);
            return trustManagerFactory;
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            throw new IllegalArgumentException("failed to create key manager factory");
        }
    }

    public static SSLContext createSSLContext(KeyManagerFactory keyManagerFactory, TrustManagerFactory trustManagerFactory) {
        // 创建 SSLContext
        try {
            var sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new IllegalArgumentException("failed to create ssl context", e);
        }
    }

    public static SSLContext createSSLContext(Path path, String password) {
        var keyStore = createKeyStore(path, password);
        var KeyManagerFactory = createKeyManagerFactory(keyStore, password);
        var trustManagerFactory = createTrustManagerFactory(keyStore);
        return createSSLContext(KeyManagerFactory, trustManagerFactory);
    }

    // 获取系统默认证书并返回 TLS 对象（用于客户端） 
    public static SSLContext createDefaultSSLContext() {
        try {
            var tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);
            var sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize default TLS configuration", e);
        }
    }

    public static SSLContext createTrustAnySSLContext() {
        // 创建自定义 TrustManager，忽略证书验证（仅用于测试环境）
        var trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            // 初始化 SSLContext
            var sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null);
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

}
