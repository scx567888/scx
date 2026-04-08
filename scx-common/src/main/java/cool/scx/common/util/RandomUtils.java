package cool.scx.common.util;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/// RandomUtils (左开右闭)
///
/// @author scx567888
/// @version 0.0.1
public final class RandomUtils {

    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public static int randomInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public static int randomInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public static int randomInt(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    public static long randomLong() {
        return ThreadLocalRandom.current().nextLong();
    }

    public static long randomLong(long bound) {
        return ThreadLocalRandom.current().nextLong(bound);
    }

    public static long randomLong(long origin, long bound) {
        return ThreadLocalRandom.current().nextLong(origin, bound);
    }

    public static float randomFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }

    public static float randomFloat(float bound) {
        return ThreadLocalRandom.current().nextFloat(bound);
    }

    public static float randomFloat(float origin, float bound) {
        return ThreadLocalRandom.current().nextFloat(origin, bound);
    }

    public static double randomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static double randomDouble(double bound) {
        return ThreadLocalRandom.current().nextDouble(bound);
    }

    public static double randomDouble(double origin, double bound) {
        return ThreadLocalRandom.current().nextDouble(origin, bound);
    }

    public static boolean randomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static void randomBytes(byte[] bytes) {
        ThreadLocalRandom.current().nextBytes(bytes);
    }

    public static byte[] randomBytes(int size) {
        var bytes = new byte[size];
        randomBytes(bytes);
        return bytes;
    }

    public static String randomString(int size, String[] pool) {
        var value = new StringBuilder();
        for (int i = 0; i < size; i = i + 1) {
            value.append(pool[randomInt(pool.length)]);
        }
        return value.toString();
    }

    public static String randomString(int size, char[] pool) {
        var value = new char[size];
        for (int i = 0; i < size; i = i + 1) {
            value[i] = pool[randomInt(pool.length)];
        }
        return new String(value);
    }

    public static String randomString(int size, String pool) {
        return randomString(size, StringUtils.split(pool));
    }

    @SafeVarargs
    public static <T> T randomGet(T... array) {
        var i = randomInt(0, array.length);
        return array[i];
    }

    public static <T> T randomGet(List<T> list) {
        var i = randomInt(0, list.size());
        return list.get(i);
    }

    public static <T> T[] randomGet(T[] array, int size) {
        if (size > array.length) {
            throw new IndexOutOfBoundsException("取出 Array 的长度必须小于 原 Array 的长度 !!!");
        }
        var c = Arrays.copyOf(array, array.length);
        ArrayUtils.shuffle((Object[]) c);
        return Arrays.copyOf(c, size);
    }

    public static <T> List<T> randomGet(List<T> list, int size) {
        if (size > list.size()) {
            throw new IndexOutOfBoundsException("取出 List 的长度必须小于 原 List 的长度 !!!");
        }
        var c = new ArrayList<>(list);
        Collections.shuffle(c);
        return c.subList(0, size);
    }

}
