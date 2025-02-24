package cool.scx.common.util;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.*;

/// Base64Utils 对 [java.util.Base64] 的薄封装
///
/// @author scx567888
/// @version 0.0.2
public final class Base64Utils {

    // ==== 标准 Base64 编码 ====
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

    // ==== URL Base64 编码（URL 和文件名安全） ====
    public static byte[] encodeUrl(String str) {
        return encodeUrl(str.getBytes(UTF_8));
    }

    public static byte[] decodeUrl(String base64) {
        return decodeUrl(base64.getBytes(UTF_8));
    }

    public static byte[] encodeUrl(byte[] bytes) {
        return getUrlEncoder().encode(bytes);
    }

    public static byte[] decodeUrl(byte[] base64) {
        return getUrlDecoder().decode(base64);
    }

    public static String encodeUrlToString(String str) {
        return encodeUrlToString(str.getBytes(UTF_8));
    }

    public static String decodeUrlToString(String base64) {
        return decodeUrlToString(base64.getBytes(UTF_8));
    }

    public static String encodeUrlToString(byte[] bytes) {
        return new String(getUrlEncoder().encode(bytes), UTF_8);
    }

    public static String decodeUrlToString(byte[] base64) {
        return new String(getUrlDecoder().decode(base64), UTF_8);
    }

    // ==== MIME Base64 编码（多行格式） ====
    public static byte[] encodeMime(String str) {
        return encodeMime(str.getBytes(UTF_8));
    }

    public static byte[] decodeMime(String base64) {
        return decodeMime(base64.getBytes(UTF_8));
    }

    public static byte[] encodeMime(byte[] bytes) {
        return getMimeEncoder().encode(bytes);
    }

    public static byte[] decodeMime(byte[] base64) {
        return getMimeDecoder().decode(base64);
    }

    public static String encodeMimeToString(String str) {
        return encodeMimeToString(str.getBytes(UTF_8));
    }

    public static String decodeMimeToString(String base64) {
        return decodeMimeToString(base64.getBytes(UTF_8));
    }

    public static String encodeMimeToString(byte[] bytes) {
        return new String(getMimeEncoder().encode(bytes), UTF_8);
    }

    public static String decodeMimeToString(byte[] base64) {
        return new String(getMimeDecoder().decode(base64), UTF_8);
    }

}
