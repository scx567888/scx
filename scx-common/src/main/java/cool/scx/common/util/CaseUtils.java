package cool.scx.common.util;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/// 命名方法转换工具类
/// 提供 驼峰 匈牙利 短横线 及 蛇形命名法的互相转换
///
/// @author scx567888
/// @version 0.0.1
public final class CaseUtils {

    /// 转换为驼峰命名法 getNameByAge
    ///
    /// @param str s
    /// @return r
    public static String toCamel(String str) {
        //就是将匈牙利命名法的首字母小写而已
        return toCamel(str, deduceCaseType(str));
    }

    /// 转换为匈牙利命名法 GetNameByAge
    ///
    /// @param str s
    /// @return r
    public static String toPascal(String str) {
        return toPascal(str, deduceCaseType(str));
    }

    /// 转换为短横线命名法 get-name-by-age
    ///
    /// @param str s
    /// @return r
    public static String toKebab(String str) {
        return toKebab(str, deduceCaseType(str));
    }

    /// 转换为蛇形命名法 get_name_by_age
    ///
    /// @param str s
    /// @return r
    public static String toSnake(String str) {
        return toSnake(str, deduceCaseType(str));
    }

    /// 转换为驼峰命名法 getNameByAge
    ///
    /// @param str      s
    /// @param caseType a [CaseType] object
    /// @return r
    public static String toCamel(String str, CaseType caseType) {
        //就是将匈牙利命名法的首字母小写而已
        return convert(str, CaseType.CAMEL_CASE, caseType);
    }

    /// 转换为匈牙利命名法 GetNameByAge
    ///
    /// @param str      s
    /// @param caseType a [CaseType] object
    /// @return r
    public static String toPascal(String str, CaseType caseType) {
        return convert(str, CaseType.PASCAL_CASE, caseType);
    }

    /// 转换为短横线命名法 get-name-by-age
    ///
    /// @param str      s
    /// @param caseType a [CaseType] object
    /// @return r
    public static String toKebab(String str, CaseType caseType) {
        return convert(str, CaseType.KEBAB_CASE, caseType);
    }

    /// 转换为蛇形命名法 get_name_by_age
    ///
    /// @param str      s
    /// @param caseType a [CaseType] object
    /// @return r
    public static String toSnake(String str, CaseType caseType) {
        return convert(str, CaseType.SNAKE_CASE, caseType);
    }

    /// 推断原来的命名是啥 并返回切割后的源字符串
    ///
    /// @param str  源字符串
    /// @param to   a [CaseType] object
    /// @param from a [CaseType] object
    /// @return 分割后的数组
    public static String convert(String str, CaseType to, CaseType from) {
        return from == to ? str : to.getString(from.getSourceStrings(str));
    }

    /// 推断字符串原有的类型
    ///
    /// @param str s
    /// @return a
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

    /// CaseType
    ///
    /// @author scx567888
    /// @version 0.0.1
    public enum CaseType implements CaseTypeHandler {

        CAMEL_CASE(new CamelCaseHandler()),
        PASCAL_CASE(new PascalCaseHandler()),
        KEBAB_CASE(new KebabCaseHandler()),
        SNAKE_CASE(new SnakeCaseHandler()),
        BLANK(new BlankHandler());

        private final CaseTypeHandler handler;

        CaseType(CaseTypeHandler handler) {
            this.handler = handler;
        }

        @Override
        public String[] getSourceStrings(String o) {
            return handler.getSourceStrings(o);
        }

        @Override
        public String getString(String[] s) {
            return handler.getString(s);
        }

    }

    private interface CaseTypeHandler {

        String[] getSourceStrings(String s);

        String getString(String[] s);

    }

    private static final class SnakeCaseHandler implements CaseTypeHandler {

        @Override
        public String[] getSourceStrings(String s) {
            return s.split("_");
        }

        @Override
        public String getString(String[] s) {
            return Arrays.stream(s).map(String::toLowerCase).collect(Collectors.joining("_"));
        }

    }

    private static class PascalCaseHandler implements CaseTypeHandler {

        /// 切割驼峰命名法的正则表达式
        ///
        /// @see <a href="https://stackoverflow.com/a/7594052">https://stackoverflow.com/a/7594052</a>
        private static final Pattern SPLIT_PATTERN = Pattern.compile("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");

        /// 将字符串的第一个字母大写 如 abc 转换为 Abc
        ///
        /// @param string s
        /// @return s
        private static String makeFirstCharUpperCase(String string) {
            var charArray = string.toCharArray();
            if ('a' <= charArray[0] && charArray[0] <= 'z') {
                charArray[0] = (char) (charArray[0] ^ 32);
            }
            return new String(charArray);
        }

        @Override
        public String[] getSourceStrings(String s) {
            return SPLIT_PATTERN.split(s);
        }

        @Override
        public String getString(String[] ss) {
            return Arrays.stream(ss).map(s -> makeFirstCharUpperCase(s.toLowerCase())).collect(Collectors.joining());
        }

    }

    private static final class BlankHandler implements CaseTypeHandler {

        private static final String[] EMPTY_ARRAY = new String[]{};

        private static final String EMPTY_STRING = "";

        @Override
        public String[] getSourceStrings(String s) {
            return EMPTY_ARRAY;
        }

        @Override
        public String getString(String[] s) {
            return EMPTY_STRING;
        }

    }

    private static final class CamelCaseHandler extends PascalCaseHandler {

        /// 将字符串的第一个字母小写 如 ABC 转换为 aBC
        ///
        /// @param string s
        /// @return s
        private static String makeFirstCharLowerCase(String string) {
            var charArray = string.toCharArray();
            if ('A' <= charArray[0] && charArray[0] <= 'Z') {
                charArray[0] = (char) (charArray[0] ^ 32);
            }
            return new String(charArray);
        }

        @Override
        public String getString(String[] s) {
            return makeFirstCharLowerCase(super.getString(s));
        }

    }

    private static final class KebabCaseHandler implements CaseTypeHandler {

        @Override
        public String[] getSourceStrings(String s) {
            return s.split("-");
        }

        @Override
        public String getString(String[] s) {
            return Arrays.stream(s).map(String::toLowerCase).collect(Collectors.joining("-"));
        }

    }

}
