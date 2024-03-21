package cool.scx.common.util.case_impl;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>PascalCaseHandler class.</p>
 *
 * @author scx567888
 * @version 0.0.8
 */
class PascalCaseHandler implements CaseTypeHandler {

    /**
     * 切割驼峰命名法的正则表达式
     *
     * @see <a href="https://stackoverflow.com/a/7594052">https://stackoverflow.com/a/7594052</a>
     */
    private static final Pattern SPLIT_PATTERN = Pattern.compile("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");

    /**
     * 将字符串的第一个字母大写 如 abc 转换为 Abc
     *
     * @param string s
     * @return s
     */
    private static String makeFirstCharUpperCase(String string) {
        var charArray = string.toCharArray();
        if ('a' <= charArray[0] && charArray[0] <= 'z') {
            charArray[0] = (char) (charArray[0] ^ 32);
        }
        return new String(charArray);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getSourceStrings(String s) {
        return SPLIT_PATTERN.split(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String[] ss) {
        return Arrays.stream(ss).map(s -> makeFirstCharUpperCase(s.toLowerCase())).collect(Collectors.joining());
    }

}
