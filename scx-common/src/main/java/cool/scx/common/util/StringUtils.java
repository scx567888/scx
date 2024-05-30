package cool.scx.common.util;

/**
 * String工具类
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class StringUtils {

    /**
     * 创建重复字符串 (带分隔符) 拓展了 {@link java.lang.String#repeat(int)} 无法添加分隔符的功能
     *
     * @param str       源字符串
     * @param delimiter 分隔符
     * @param count     重复次数
     * @return 结果
     */
    public static String repeat(String str, String delimiter, int count) {
        if (count == 0) {
            return "";
        }
        var element = str + delimiter;
        var result = element.repeat(count);
        return result.substring(0, result.length() - delimiter.length());
    }

    /**
     * 校验字符串是否不为 null 且不全为空白 (空格 " ")
     *
     * @param str 待检查的字符串
     * @return a boolean.
     */
    public static boolean notBlank(String str) {
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
    public static boolean notEmpty(String str) {
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
     * startsWith (忽略大小写)
     *
     * @param str    s
     * @param prefix s
     * @return s
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return str.length() >= prefix.length() && str.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    /**
     * endWith (忽略大小写)
     *
     * @param str    s
     * @param suffix s
     * @return s
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return str.length() >= suffix.length() && str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length());
    }

    /**
     * 联接字符串 但是不连接空
     *
     * @param strings str1
     * @return str
     */
    public static String concat(String... strings) {
        var sb = new StringBuilder();
        for (var string : strings) {
            if (string != null) {
                sb.append(string);
            }
        }
        return sb.toString();
    }

    /**
     * 将字符串拆分为字符 (按照码点拆分, 可正确处理 emoji)
     *
     * @param str 字符串
     * @return 拆分后的字符数组
     */
    public static String[] split(String str) {
        var codePoints = str.codePoints().toArray();
        var value = new String[codePoints.length];
        for (var i = 0; i < codePoints.length; i = i + 1) {
            value[i] = Character.toString(codePoints[i]);
        }
        return value;
    }

}
