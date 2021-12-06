package cool.scx.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class Base64Utils {

    /**
     * base 64
     */
    private static final Base64.Encoder defaultBase64Encoder = Base64.getEncoder();

    /**
     * base 64
     */
    private static final Base64.Decoder defaultBase64Decoder = Base64.getDecoder();

    /**
     * 根据 字符串 获取(加密) BASE64
     *
     * @param str a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String encode(String str) {
        return encode(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 根据 BASE64 获取(解密) 字符串
     *
     * @param base64 a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String decode(String base64) {
        return new String(defaultBase64Decoder.decode(base64), StandardCharsets.UTF_8);
    }

    /**
     * 根据 byte 获取(加密) BASE64
     *
     * @param bytes a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String encode(byte[] bytes) {
        return new String(defaultBase64Encoder.encode(bytes), StandardCharsets.UTF_8);
    }

}
