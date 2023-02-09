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
     * 代替 MAP 实现更高的性能 索引为 char, 值为 byte, 因数组较小, 所以此处直接硬编码
     * <br>
     * 数据获得方法如下
     * <pre>{@code
     *  var CHAR_BYTE_MAP = new byte['F' + 1];
     *  Arrays.fill(CHAR_BYTE_MAP, (byte) -1);
     *  for (int i = 0; i < HEX_CHAR_POOL.length; i = i + 1) {
     *      CHAR_BYTE_MAP[HEX_CHAR_POOL[i]] = (byte) i;
     *  }
     * }</pre>
     */
    private static final byte[] CHAR_BYTE_MAP = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15};

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
            // 此处需要做一次掩码计算, 因为在 java 中, byte 在位移时会先转换为 int 再计算 .
            // 如 10 进制数 -123,  [1000_0101] -> [1111_1111_1111_1111_1111_1111_1000_0101] (前端被补 1) .
            // b >>> 4 则为 [0000_1111_1111_1111_1111_1111_1111_1000] (最前端补0), 而不是理想的 [0000_1000] .
            // 掩码计算便是为了去除高位的 0 .
            // chars[charsIndex] = HEX_CHAR_POOL[(b & 0b1111_1111) >>> 4]; // 这种形式亦可 .
            chars[charsIndex] = HEX_CHAR_POOL[b >>> 4 & 0b1111];
            chars[charsIndex + 1] = HEX_CHAR_POOL[b & 0b1111];
            charsIndex = charsIndex + 2;
            bytesIndex = bytesIndex + 1;
        }
        return new String(chars, StandardCharsets.UTF_8);
    }

}
