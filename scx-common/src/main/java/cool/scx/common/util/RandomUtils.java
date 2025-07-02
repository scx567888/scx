package cool.scx.common.util;

import cool.scx.collections.ArrayUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/// 用于生成简单的随机数
///
/// @author scx567888
/// @version 0.0.1
public final class RandomUtils {

    /// 获取随机的 字符串
    /// 注意!!! 此方法和 getUUID 不同 若需要获取 uuid 请使用 getUUID
    ///
    /// @param size     字符串 的长度
    /// @param poolType 候选池类型
    /// @return 随机的 字符串
    public static String randomString(int size, PoolType poolType) {
        var pool = poolType.pool;
        var value = new char[size];
        for (int i = 0; i < size; i = i + 1) {
            value[i] = pool[randomInt(pool.length)];
        }
        return new String(value);
    }

    /// 获取随机的 字符串 (包含字母和数字)
    ///
    /// @param size 长度
    /// @return 随机的 字符串
    public static String randomString(int size) {
        return randomString(size, PoolType.NUMBER_AND_LETTER);
    }

    /// 获取随机的 字符串
    ///
    /// @param size 长度
    /// @param pool 候选池 (会被切割成单个字符)
    /// @return 随机的 字符串
    public static String randomString(int size, String pool) {
        return randomString(size, StringUtils.split(pool));
    }

    /// 获取随机的 字符串
    ///
    /// @param size 长度
    /// @param pool 候选池
    /// @return 随机的 字符串
    public static String randomString(int size, String[] pool) {
        var value = new StringBuilder();
        for (int i = 0; i < size; i = i + 1) {
            value.append(pool[randomInt(pool.length)]);
        }
        return value.toString();
    }

    /// 获取 UUID
    ///
    /// @return UUID
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    /// 随机从数组中取出一个元素
    ///
    /// @param array 数组
    /// @param <T>   T
    /// @return 随机的元素
    @SafeVarargs
    public static <T> T randomGet(T... array) {
        var i = randomInt(0, array.length);
        return array[i];
    }

    /// 随机从列表中取出一个元素
    ///
    /// @param list 列表
    /// @param <T>  T
    /// @return 随机的元素
    public static <T> T randomGet(List<T> list) {
        var i = randomGet(0, list.size());
        return list.get(i);
    }

    /// 随机从数组中取出 多个元素 (不会重复)
    ///
    /// @param array 数组
    /// @param size  长度
    /// @param <T>   t
    /// @return a
    public static <T> T[] randomGet(T[] array, int size) {
        if (size > array.length) {
            throw new IndexOutOfBoundsException("取出 Array 的长度必须小于 原 Array 的长度 !!!");
        }
        var c = Arrays.copyOf(array, array.length);
        ArrayUtils.shuffle((Object[]) c);
        return Arrays.copyOf(c, size);
    }

    /// 随机从数组中取出 多个元素 (不会重复)
    ///
    /// @param list 数组
    /// @param size 长度
    /// @param <T>  a
    /// @return a
    public static <T> List<T> randomGet(List<T> list, int size) {
        if (size > list.size()) {
            throw new IndexOutOfBoundsException("取出 List 的长度必须小于 原 List 的长度 !!!");
        }
        var c = new ArrayList<>(list);
        Collections.shuffle(c);
        return c.subList(0, size);
    }

    public static boolean randomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    /// 生成一个新的随机 byte 数组
    ///
    /// @param size 长度
    /// @return byte 数组
    public static byte[] randomBytes(int size) {
        var bytes = new byte[size];
        randomBytes(bytes);
        return bytes;
    }

    /// 填充用户提供的 byte 数组, 范围 -128 (包含) 到 127 (包含)
    ///
    /// @param bytes bytes
    public static void randomBytes(byte[] bytes) {
        ThreadLocalRandom.current().nextBytes(bytes);
    }

    public static float randomFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }

    public static float randomFloat(float bound) {
        return ThreadLocalRandom.current().nextFloat(bound);
    }

    /// 返回随机数
    ///
    /// @param origin 起始 (包含)
    /// @param bound  结束 (不包含)
    /// @return int
    public static float randomFloat(float origin, float bound) {
        return ThreadLocalRandom.current().nextFloat(origin, bound);
    }

    public static double randomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static double randomDouble(double bound) {
        return ThreadLocalRandom.current().nextDouble(bound);
    }

    /// 返回随机数
    ///
    /// @param origin 起始 (包含)
    /// @param bound  结束 (不包含)
    /// @return int
    public static double randomDouble(double origin, double bound) {
        return ThreadLocalRandom.current().nextDouble(origin, bound);
    }

    public static int randomInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public static int randomInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    /// 返回随机数
    ///
    /// @param origin 起始 (包含)
    /// @param bound  结束 (不包含)
    /// @return int
    public static int randomInt(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    public static long randomLong() {
        return ThreadLocalRandom.current().nextLong();
    }

    public static long randomLong(long bound) {
        return ThreadLocalRandom.current().nextLong(bound);
    }

    /// 返回随机数
    ///
    /// @param origin 起始 (包含)
    /// @param bound  结束 (不包含)
    /// @return int
    public static long randomLong(long origin, long bound) {
        return ThreadLocalRandom.current().nextLong(origin, bound);
    }

    public enum PoolType {

        NUMBER("0123456789"),
        LETTER("ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
        NUMBER_AND_LETTER("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        final char[] pool;

        PoolType(String str) {
            this.pool = str.toCharArray();
        }

    }

}
