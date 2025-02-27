package cool.scx.tcp.tls;

import cool.scx.common.util.ArrayUtils;
import cool.scx.common.util.Base64Utils;

import java.util.regex.Pattern;

public final class PemHelper {

    public static final Pattern PEM_PATTERN = Pattern.compile("-----BEGIN (.+?)-----([\\s\\S]+?)-----END \\1-----");

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

}
