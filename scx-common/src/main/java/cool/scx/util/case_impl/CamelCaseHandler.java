package cool.scx.util.case_impl;

/**
 * <p>CamelCaseHandler class.</p>
 *
 * @author scx567888
 * @version 0.0.8
 */
final class CamelCaseHandler extends PascalCaseHandler {

    /**
     * 将字符串的第一个字母小写 如 ABC 转换为 aBC
     *
     * @param string s
     * @return s
     */
    private static String makeFirstCharLowerCase(String string) {
        var charArray = string.toCharArray();
        if ('A' <= charArray[0] && charArray[0] <= 'Z') {
            charArray[0] = (char) (charArray[0] ^ 32);
        }
        return new String(charArray);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String[] s) {
        return makeFirstCharLowerCase(super.getString(s));
    }

}
