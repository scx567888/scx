package cool.scx.util;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * String工具类
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class StringUtils {

    /**
     * 校验字符串是否不为空
     *
     * @param str 待检查的字符串
     * @return a boolean.
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 校验字符串是否为空
     *
     * @param str a {@link java.lang.Object} object.
     * @return a boolean.
     */
    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
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