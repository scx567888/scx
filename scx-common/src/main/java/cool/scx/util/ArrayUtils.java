package cool.scx.util;

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

}
