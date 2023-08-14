package cool.scx.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cool.scx.util.RandomUtils.nextInt;

/**
 * 提供一些 Array 的方法, 也有一些 List 相关的方法
 */
public final class ArrayUtils {

    /**
     * indexOf 数组版本
     *
     * @param a  原数组
     * @param a1 带查找的数组
     * @return 索引
     */
    public static int indexOf(byte[] a, byte[] a1) {
        for (int i = 0; i <= a.length - a1.length; i = i + 1) {
            var found = true;
            for (int j = 0; j < a1.length; j = j + 1) {
                if (a[i + j] != a1[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(Object[] a, Object[] a1) {
        for (int i = 0; i <= a.length - a1.length; i = i + 1) {
            var found = true;
            for (int j = 0; j < a1.length; j = j + 1) {
                if (!Objects.equals(a[i + j], a1[j])) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public static byte[] toPrimitive(Byte[] w) {
        var p = new byte[w.length];
        for (int i = 0; i < w.length; i = i + 1) {
            p[i] = w[i];
        }
        return p;
    }

    public static long[] toPrimitive(Long[] w) {
        var p = new long[w.length];
        for (int i = 0; i < w.length; i = i + 1) {
            p[i] = w[i];
        }
        return p;
    }

    public static long[] toPrimitive(List<Long> w) {
        var p = new long[w.size()];
        for (int i = 0; i < w.size(); i = i + 1) {
            p[i] = w.get(i);
        }
        return p;
    }

    public static Byte[] toWrapper(byte[] p) {
        var w = new Byte[p.length];
        for (int i = 0; i < p.length; i = i + 1) {
            w[i] = p[i];
        }
        return w;
    }

    public static long[] toLongArray(int[] intArray) {
        if (intArray == null) {
            return null;
        }
        long[] longArray = new long[intArray.length];
        for (int i = 0; i < intArray.length; i = i + 1) {
            longArray[i] = intArray[i];
        }
        return longArray;
    }

    public static <T> T[] concat(T[] first, T[] second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * 按照指定份数切割 List, 注意和 {@link ArrayUtils#splitList(List, int)} 进行区分
     *
     * @param list list
     * @param n    份数
     * @param <T>  T
     * @return 切割后的 list
     */
    public static <T> List<List<T>> splitListN(List<T> list, int n) {
        List<List<T>> result = new ArrayList<>();
        int rem = list.size() % n;
        int size = list.size() / n;
        int idx = 0;
        for (int i = 0; i < n; i = i + 1) {
            int curSize = size;
            if (rem > 0) {
                curSize = curSize + 1;
                rem = rem - 1;
            }
            result.add(list.subList(idx, idx + curSize));
            idx = idx + curSize;
        }
        return result;
    }

    /**
     * 按照指定长度切割 List, 注意和 {@link ArrayUtils#splitListN(List, int)}} 进行区分
     *
     * @param list list
     * @param size 每份的长度
     * @param <T>  T
     * @return 切割后的 list
     */
    public static <T> List<List<T>> splitList(List<T> list, int size) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i = i + size) {
            int end = Math.min(i + size, list.size());
            result.add(list.subList(i, end));
        }
        return result;
    }

    /***
     * shuffle
     * @param arr a
     */
    public static void shuffle(Object[] arr) {
        int size = arr.length;
        for (int i = size; i > 1; i = i - 1) {
            swap(arr, i - 1, nextInt(i));
        }
    }

    /**
     * 交换元素
     *
     * @param arr a
     * @param i   a
     * @param j   a
     */
    public static void swap(Object[] arr, int i, int j) {
        var tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

}
