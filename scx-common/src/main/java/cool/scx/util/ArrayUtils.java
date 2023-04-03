package cool.scx.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

}
