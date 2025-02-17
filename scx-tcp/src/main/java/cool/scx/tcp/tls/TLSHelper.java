package cool.scx.tcp.tls;

import cool.scx.common.util.ArrayUtils;
import cool.scx.common.util.Base64Utils;
import cool.scx.common.util.RandomUtils;

import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.regex.Pattern;


/**
 * TLSHelper
 *
 * @author scx567888
 * @version 0.0.1
 */
public class TLSHelper {

    public static final Pattern PEM_PATTERN = Pattern.compile("-----BEGIN (.+?)-----([\\s\\S]+?)-----END \\1-----");

    public static KeyStore createKeyStore(Path path, String password) {
        // 证书存储器
        try {
            return KeyStore.getInstance(path.toFile(), password.toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new IllegalArgumentException("failed to create keystore ", e);
        }
    }

    /**
     * 创建 KetStore
     *
     * @param certificate 公钥证书
     * @param privateKey  私钥证书 (可为空)
     * @param password    密码 (当私钥为空时可为空)
     * @return KetStore
     */
    public static KeyStore createKeyStore(X509Certificate certificate, PrivateKey privateKey, String password) {
        try {
            var keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(null, null); // 初始化空的 KeyStore
            //私钥证书是可选的
            if (privateKey != null) {
                // 存储私钥和证书（用于服务器端或需要客户端身份验证的情况）
                keyStore.setKeyEntry("default", privateKey, password.toCharArray(), new Certificate[]{certificate});
            } else {
                // 仅存储证书（用于客户端信任库）
                keyStore.setCertificateEntry("default", certificate);
            }
            return keyStore;
        } catch (Exception e) {
            throw new IllegalArgumentException("failed to create keystore ", e);
        }
    }

    public static X509Certificate createCertificateFromPem(String pemContent) {
        // 解析证书
        var certPemObject = parsePem(pemContent);
        if (!"CERTIFICATE".equals(certPemObject.marker())) {
            throw new IllegalArgumentException("PEM 内容不是证书");
        }
        try {
            var factory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certPemObject.content()));
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    public static PrivateKey createPrivateKeyFromPem(String pemContent) {
        // 解析私钥
        var keyPemObject = parsePem(pemContent);

        byte[] encodedKey;

        //根据不同的 marker 处理
        switch (keyPemObject.marker()) {
            case "PRIVATE KEY" -> encodedKey = keyPemObject.content();
            case "RSA PRIVATE KEY" -> encodedKey = convertPKCS1ToPKCS8(keyPemObject.content()); // 未加密的 PKCS#1 私钥，需要转换
            default -> throw new IllegalArgumentException("不支持的私钥格式：" + keyPemObject.marker());
        }

        var keySpec = new PKCS8EncodedKeySpec(encodedKey);
        try {
            var keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
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
        return createSSLContext(keyStore, password);
    }

    public static SSLContext createSSLContext(KeyStore keyStore, String password) {
        var KeyManagerFactory = createKeyManagerFactory(keyStore, password);
        var trustManagerFactory = createTrustManagerFactory(keyStore);
        return createSSLContext(KeyManagerFactory, trustManagerFactory);
    }

    /**
     * 根据 pem 格式证书 创建 SSLContext
     *
     * @param certPemPath 公钥证书
     * @param keyPemPath  私钥证书 (可选的 可穿 null)
     * @return SSLContext
     */
    public static SSLContext createSSLContextFromPem(Path certPemPath, Path keyPemPath) {
        try {
            var certPemContent = Files.readString(certPemPath);
            //私钥是可选的
            var keyPemContent = keyPemPath != null ? Files.readString(keyPemPath) : null;
            return createSSLContextFromPem(certPemContent, keyPemContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static SSLContext createSSLContextFromPem(String certPemContent, String keyPemContent) {
        return createSSLContextFromPem(certPemContent, keyPemContent, RandomUtils.randomString(6));
    }

    public static SSLContext createSSLContextFromPem(String certPemContent, String keyPemContent, String keyPassword) {
        var certificate = createCertificateFromPem(certPemContent);
        //私钥是可选的
        var privateKey = keyPemContent != null ? createPrivateKeyFromPem(keyPemContent) : null;
        // 创建 KeyStore
        var keyStore = createKeyStore(certificate, privateKey, keyPassword);
        // 创建 SSLContext
        return createSSLContext(keyStore, keyPassword);
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
            throw new RuntimeException("Failed to initialize default TLS configuration", e);
        }
    }

    public static SSLContext createTrustAnySSLContext() {
        // 创建自定义 TrustManager，忽略证书验证（仅用于测试环境）
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
            throw new RuntimeException(e);
        }
    }

    public static PemObject parsePem(String pemContent) throws IllegalArgumentException {
        // 正则表达式，用于匹配 PEM 格式
        var matcher = PEM_PATTERN.matcher(pemContent.trim());

        if (matcher.find()) {
            var marker = matcher.group(1); // 提取标记，例如 "CERTIFICATE" 或 "PRIVATE KEY"
            var base64Content = matcher.group(2).replaceAll("\\s", ""); // 去除所有空白字符
            byte[] content = Base64Utils.decode(base64Content);
            return new PemObject(content, marker);
        } else {
            throw new IllegalArgumentException("无法解析 PEM 内容：未找到有效的标记");
        }
    }

    public static byte[] convertPKCS1ToPKCS8(byte[] pkcs1Bytes) {
        // 构造 PKCS#8 私钥的头部信息
        final byte[] pkcs8Header = new byte[]{0x30, (byte) 0x82, // SEQUENCE 标签和长度占位
                0x00, 0x00,       // 总长度，占位符
                0x02, 0x01, 0x00, // Version: INTEGER 0
                0x30, 0x0d,       // SEQUENCE
                0x06, 0x09,       // OID: OBJECT IDENTIFIER
                0x2a, (byte) 0x86, 0x48, (byte) 0x86, (byte) 0xf7, 0x0d, 0x01, 0x01, 0x01, // 1.2.840.113549.1.1.1 (RSA)
                0x05, 0x00,       // NULL
                0x04, (byte) 0x82, // OCTET STRING 标签和长度占位
                0x00, 0x00        // 私钥长度，占位符
        };

        int pkcs1Length = pkcs1Bytes.length;
        int totalLength = pkcs1Length + pkcs8Header.length - 4; // 减去占位符的长度

        // 设置长度信息
        pkcs8Header[2] = (byte) ((totalLength >> 8) & 0xFF);
        pkcs8Header[3] = (byte) (totalLength & 0xFF);
        pkcs8Header[pkcs8Header.length - 2] = (byte) ((pkcs1Length >> 8) & 0xFF);
        pkcs8Header[pkcs8Header.length - 1] = (byte) (pkcs1Length & 0xFF);

        // 组合头部和私钥内容
        return ArrayUtils.concat(pkcs8Header, pkcs1Bytes);
    }

    /**
     * 存储解析出来的 PEM 文件内容
     *
     * @param content PEM 中的二进制数据
     * @param marker  PEM 的标记，例如 "CERTIFICATE"
     */
    public record PemObject(byte[] content, String marker) {

    }

}
