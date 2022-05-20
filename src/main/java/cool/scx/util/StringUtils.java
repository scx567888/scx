package cool.scx.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * String工具类
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class StringUtils {

    /**
     * 创建重复字符串
     *
     * @param str   源字符串
     * @param count 重复次数
     * @return 结果
     */
    public static String repeat(String str, int count) {
        return repeat(str, count, "");
    }

    /**
     * 创建重复字符串
     *
     * @param str       源字符串
     * @param count     重复次数
     * @param delimiter 分隔符
     * @return 结果
     */
    public static String repeat(final String str, final int count, final String delimiter) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(delimiter);
        if (count <= 1) {
            if (count < 0) {
                throw new IllegalArgumentException("无效的 count: " + count);
            }
            return count == 0 ? "" : str;
        }

        final int strLen = str.length();
        final int delimiterLen = delimiter.length();
        final int len = strLen + delimiterLen;
        final long longSize = (long) len * (long) (count - 1) + strLen;
        final int size = (int) longSize;
        if (size != longSize) {
            throw new ArrayIndexOutOfBoundsException("所需数组 size 太大: " + longSize);
        }

        final char[] array = new char[size];
        str.getChars(0, strLen, array, 0);
        delimiter.getChars(0, delimiterLen, array, strLen);
        int n;
        for (n = len; n < size - n; n <<= 1) {
            System.arraycopy(array, 0, array, n, n);
        }
        System.arraycopy(array, 0, array, n, size - n);
        return new String(array);
    }

    /**
     * 校验字符串是否不为 null 且不全为空白 (空格 " ")
     *
     * @param str 待检查的字符串
     * @return a boolean.
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 校验字符串是否为 null 或全为空白 (空格 " ")
     *
     * @param str a {@link java.lang.Object} object.
     * @return a boolean.
     */
    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    /**
     * 校验字符串是否不为 null 并且不为空字符串 ("")
     *
     * @param str s
     * @return s
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 校验字符串是否为 null 或为空字符串 ("")
     *
     * @param str s
     * @return s
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 清理分隔符错误的路径如 清理前 : a/b//c -- 清理后 : /a/b/c
     *
     * @param url 需要清理的 url 集合
     * @return 清理后的结果
     */
    public static String cleanHttpURL(String... url) {
        return Arrays.stream(String.join("/", url).split("/")).filter(StringUtils::isNotBlank).collect(Collectors.joining("/", "/", ""));
    }

}