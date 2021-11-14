package cool.scx.util;

import java.util.Random;
import java.util.UUID;

/**
 * <p>RandomUtils class.</p>
 *
 * @author scx567888
 * @version 1.7.3
 */
public final class RandomUtils {

    private static final Random RANDOM = new Random();

    /**
     * 仅数字
     */
    private static final char[] NUMBER_POOL = "0123456789".toCharArray();

    /**
     * 数字和字母
     */
    private static final char[] NUMBER_AND_LETTER_POOL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * 获取随机的 Code
     * 注意!!! 此方法和 getUUID 不同 若需要获取 uuid 请使用 getUUID
     *
     * @param size       code 的长度
     * @param withLetter code 中是否包含字母
     * @return a {@link java.lang.String} object
     */
    public static String getRandomString(int size, boolean withLetter) {
        var code = new StringBuilder();
        var pool = withLetter ? NUMBER_AND_LETTER_POOL : NUMBER_POOL;
        for (int j = 0; j < size; j++) {
            code.append(pool[RANDOM.nextInt(pool.length)]);
        }
        return code.toString();
    }

    /**
     * 获取随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 返回再此区间的数值
     */
    public static int getRandomNumber(int min, int max) {
        return RANDOM.nextInt(max) + min;
    }

    /**
     * 获取UUID
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

}
