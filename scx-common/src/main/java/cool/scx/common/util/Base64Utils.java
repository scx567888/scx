package cool.scx.common.util;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;

/**
 * Base64Utils 对 {@link java.util.Base64} 的薄封装
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class Base64Utils {

    public static byte[] encode(String str) {
        return encode(str.getBytes(UTF_8));
    }

    public static byte[] decode(String base64) {
        return decode(base64.getBytes(UTF_8));
    }

    public static byte[] encode(byte[] bytes) {
        return getEncoder().encode(bytes);
    }

    public static byte[] decode(byte[] base64) {
        return getDecoder().decode(base64);
    }

    public static String encodeToString(String str) {
        return encodeToString(str.getBytes(UTF_8));
    }

    public static String decodeToString(String base64) {
        return decodeToString(base64.getBytes(UTF_8));
    }

    public static String encodeToString(byte[] bytes) {
        return new String(getEncoder().encode(bytes), UTF_8);
    }

    public static String decodeToString(byte[] base64) {
        return new String(getDecoder().decode(base64), UTF_8);
    }

}
