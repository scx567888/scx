package cool.scx.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * <p>HexUtils class.</p>
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class HexUtils {

    /**
     * HEX 索引表
     */
    private static final byte[] HEX_CHAR_POOL = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 代替 MAP 实现更高的性能 索引为 char, 值为 byte
     */
    private static final byte[] CHAR_BYTE_MAP = initCharByteMap();

    /**
     * 初始化
     *
     * @return a
     */
    private static byte[] initCharByteMap() {
        var chars = new byte['F' + 1];
        Arrays.fill(chars, (byte) -1);
        for (int i = 0; i < HEX_CHAR_POOL.length; i = i + 1) {
            chars[HEX_CHAR_POOL[i]] = (byte) i;
        }
        return chars;
    }

    /**
     * 十六进制转字节数组
     *
     * @param hex hex
     * @return r
     */
    public static byte[] toBytes(String hex) {
        var chars = hex.toCharArray();
        var bytes = new byte[chars.length / 2];
        int charsIndex = 0;
        int bytesIndex = 0;
        while (bytesIndex < bytes.length) {
            var highBit = CHAR_BYTE_MAP[chars[charsIndex]];
            var lowBit = CHAR_BYTE_MAP[chars[charsIndex + 1]];
            bytes[bytesIndex] = (byte) (highBit << 4 | lowBit);
            charsIndex = charsIndex + 2;
            bytesIndex = bytesIndex + 1;
        }
        return bytes;
    }

    /**
     * 字节数组转十六进制
     *
     * @param bytes an array of {@link byte} objects.
     * @return a {@link java.lang.String} object.
     */
    public static String toHex(final byte[] bytes) {
        var chars = new byte[bytes.length * 2];
        var charsIndex = 0;
        int bytesIndex = 0;
        while (bytesIndex < bytes.length) {
            var b = bytes[bytesIndex];
            chars[charsIndex] = HEX_CHAR_POOL[b >>> 4 & 0xF];
            chars[charsIndex + 1] = HEX_CHAR_POOL[b & 0xF];
            charsIndex = charsIndex + 2;
            bytesIndex = bytesIndex + 1;
        }
        return new String(chars, StandardCharsets.UTF_8);
    }

}
