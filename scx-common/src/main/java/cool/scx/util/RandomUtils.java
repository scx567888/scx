package cool.scx.util;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static cool.scx.util.ArrayUtils.shuffle;

/**
 * 用于生成简单的随机数
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class RandomUtils {

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
    public static String randomString(int size, boolean withLetter) {
        var code = new StringBuilder();
        var pool = withLetter ? NUMBER_AND_LETTER_POOL : NUMBER_POOL;
        for (int i = 0; i < size; i = i + 1) {
            code.append(pool[nextInt(pool.length)]);
        }
        return code.toString();
    }

    /**
     * 获取随机的 Code (包含字母和数字)
     *
     * @param size code 的长度
     * @return a
     */
    public static String randomString(int size) {
        return randomString(size, true);
    }

    /**
     * 取一个指定区间的随机数 (包含两端)
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    public static int randomNumber(int min, int max) {
        return nextInt(min, max + 1);
    }

    /**
     * 获取UUID
     *
     * @return a {@link java.lang.String} object.
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 随机从数组中取出一个元素
     *
     * @param array 数组
     * @param <T>   T
     * @return 随机的元素
     */
    @SafeVarargs
    public static <T> T randomGet(T... array) {
        int i = nextInt(0, array.length);
        return array[i];
    }

    /**
     * 随机从列表中取出一个元素
     *
     * @param list 列表
     * @param <T>  T
     * @return 随机的元素
     */
    public static <T> T randomGet(List<T> list) {
        int i = nextInt(0, list.size());
        return list.get(i);
    }

    /**
     * 随机从数组中取出 多个元素 (不会重复)
     *
     * @param array 数组
     * @param size  长度
     * @param <T>   t
     * @return a
     */
    public static <T> T[] randomGet(T[] array, int size) {
        if (size > array.length) {
            throw new IndexOutOfBoundsException("取出 Array 的长度必须小于 原 Array 的长度 !!!");
        }
        T[] t = Arrays.copyOf(array, array.length);
        shuffle(t);
        return Arrays.copyOf(t, size);
    }

    /**
     * 随机从数组中取出 多个元素 (不会重复)
     *
     * @param list 数组
     * @param size 长度
     * @param <T>  a
     * @return a
     */
    public static <T> List<T> randomGet(List<T> list, int size) {
        if (size > list.size()) {
            throw new IndexOutOfBoundsException("取出 List 的长度必须小于 原 List 的长度 !!!");
        }
        var r = new ArrayList<>(list);
        Collections.shuffle(r);
        return r.subList(0, size);
    }

    public static int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public static int nextInt(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

}
