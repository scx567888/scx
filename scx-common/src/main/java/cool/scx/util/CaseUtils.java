package cool.scx.util;

import cool.scx.util.case_impl.CaseType;

/**
 * 命名方法转换工具类 <br>
 * 提供 驼峰 匈牙利 短横线 及 蛇形命名法的互相转换
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class CaseUtils {

    /**
     * 转换为驼峰命名法 getNameByAge
     *
     * @param str s
     * @return r
     */
    public static String toCamel(String str) {
        //就是将匈牙利命名法的首字母小写而已
        return toCamel(str, deduceCaseType(str));
    }

    /**
     * 转换为匈牙利命名法 GetNameByAge
     *
     * @param str s
     * @return r
     */
    public static String toPascal(String str) {
        return toPascal(str, deduceCaseType(str));
    }

    /**
     * 转换为短横线命名法 get-name-by-age
     *
     * @param str s
     * @return r
     */
    public static String toKebab(String str) {
        return toKebab(str, deduceCaseType(str));
    }

    /**
     * 转换为蛇形命名法 get_name_by_age
     *
     * @param str s
     * @return r
     */
    public static String toSnake(String str) {
        return toSnake(str, deduceCaseType(str));
    }

    /**
     * 转换为驼峰命名法 getNameByAge
     *
     * @param str      s
     * @param caseType a {@link cool.scx.util.case_impl.CaseType} object
     * @return r
     */
    public static String toCamel(String str, CaseType caseType) {
        //就是将匈牙利命名法的首字母小写而已
        return convert(str, CaseType.CAMEL_CASE, caseType);
    }

    /**
     * 转换为匈牙利命名法 GetNameByAge
     *
     * @param str      s
     * @param caseType a {@link cool.scx.util.case_impl.CaseType} object
     * @return r
     */
    public static String toPascal(String str, CaseType caseType) {
        return convert(str, CaseType.PASCAL_CASE, caseType);
    }

    /**
     * 转换为短横线命名法 get-name-by-age
     *
     * @param str      s
     * @param caseType a {@link cool.scx.util.case_impl.CaseType} object
     * @return r
     */
    public static String toKebab(String str, CaseType caseType) {
        return convert(str, CaseType.KEBAB_CASE, caseType);
    }

    /**
     * 转换为蛇形命名法 get_name_by_age
     *
     * @param str      s
     * @param caseType a {@link cool.scx.util.case_impl.CaseType} object
     * @return r
     */
    public static String toSnake(String str, CaseType caseType) {
        return convert(str, CaseType.SNAKE_CASE, caseType);
    }

    /**
     * 推断原来的命名是啥 并返回切割后的源字符串
     *
     * @param str  源字符串
     * @param to   a {@link cool.scx.util.case_impl.CaseType} object
     * @param from a {@link cool.scx.util.case_impl.CaseType} object
     * @return 分割后的数组
     */
    public static String convert(String str, CaseType to, CaseType from) {
        return from == to ? str : to.getString(from.getSourceStrings(str));
    }

    /**
     * 推断字符串原有的类型
     *
     * @param str s
     * @return a
     */
    private static CaseType deduceCaseType(String str) {
        if (StringUtils.isBlank(str)) {
            return CaseType.BLANK;
        } else if (str.contains("_")) { // 以下划线分割 如 a_b
            return CaseType.SNAKE_CASE;
        } else if (str.contains("-")) { //以短横线分割 如 a-b
            return CaseType.KEBAB_CASE;
        } else { //其余情形全当作 驼峰进行处理
            return CaseType.PASCAL_CASE;
        }
    }

}
