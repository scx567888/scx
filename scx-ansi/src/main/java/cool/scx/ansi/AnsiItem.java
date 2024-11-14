package cool.scx.ansi;

import java.util.StringJoiner;

import static cool.scx.ansi.AnsiStyle.RESET;

public record AnsiItem(Object value, AnsiElement... elements) {

    /**
     * 起始字符
     */
    private static final String ENCODE_START = "\033[";

    /**
     * 合并字符
     */
    private static final String ENCODE_JOIN = ";";

    /**
     * 结束字符
     */
    private static final String ENCODE_END = "m";

    public static String joinAnsiElement(AnsiElement... elements) {
        var joiner = new StringJoiner(ENCODE_JOIN);
        for (var element : elements) {
            joiner.add(element.code());
        }
        return joiner.toString();
    }

    public void buildEnabled(StringBuilder sb) {
        //如果没有转义符 则直接返回
        if (elements.length == 0) {
            buildDisabled(sb);
            return;
        }

        //1, 添加 ansi 转义符
        sb.append(ENCODE_START);
        sb.append(joinAnsiElement(elements));
        sb.append(ENCODE_END);

        //2, 添加文字内容
        sb.append(value);

        //3, 添加重置转义符
        sb.append(ENCODE_START);
        sb.append(RESET.code());
        sb.append(ENCODE_END);
    }

    public void buildDisabled(StringBuilder sb) {
        sb.append(value);
    }

}
