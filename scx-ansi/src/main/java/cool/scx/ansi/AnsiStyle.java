package cool.scx.ansi;

/**
 * Ansi 样式
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum AnsiStyle implements AnsiElement {

    NORMAL("0"),
    BOLD("1"),
    FAINT("2"),
    ITALIC("3"),
    UNDERLINE("4");

    private final String code;

    AnsiStyle(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

}
