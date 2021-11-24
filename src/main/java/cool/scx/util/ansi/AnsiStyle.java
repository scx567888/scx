package cool.scx.util.ansi;

/**
 * Ansi 样式
 */
public enum AnsiStyle implements AnsiElement {

    /**
     * 标准
     */
    NORMAL("0"),

    /**
     * 加粗
     */
    BOLD("1"),

    /**
     * 细线
     */
    FAINT("2"),

    /**
     * 斜体
     */
    ITALIC("3"),

    /**
     * 下划线
     */
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
