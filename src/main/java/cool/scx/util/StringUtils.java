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
     * @param sourceStr 源字符串
     * @param count     重复次数
     * @param delimiter 分隔符
     * @return 结果
     */
    public static String repeat(String sourceStr, int count, String delimiter) {
        Objects.requireNonNull(sourceStr);
        Objects.requireNonNull(delimiter);
        if (count <= 1) {
            if (count < 0) {
                throw new IllegalArgumentException("无效的 count: " + count);
            }
            return count == 0 ? "" : sourceStr;
        }

        var element = sourceStr + delimiter;//我们将一个原始字符串和一个分隔符拼接为一个整体并称之为 element
        var elementLength = element.length();
        var longSize = (long) elementLength * count - delimiter.length();// 减去最后的分割符的长度即为总长度
        var size = (int) longSize;
        if (size != longSize) {
            throw new ArrayIndexOutOfBoundsException("生成的字符串长度超出上限 : " + longSize);
        }

        char[] array = new char[size];
        element.getChars(0, elementLength, array, 0);//填充原始 char 数据
        for (int n = elementLength; n < size; n = n * 2) {//指数增长的方式复制填充
            System.arraycopy(array, 0, array, n, Math.min(n, size - n));
        }
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