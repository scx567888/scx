package cool.scx.util.ansi;

/**
 * 8Bit 前景色
 */
public final class Ansi8BitColor implements AnsiElement {

    private final String prefix;

    private final int code;

    private Ansi8BitColor(String prefix, int code) {
        if (code < 0 || code > 255) {
            throw new IllegalArgumentException("Code must be between 0 and 255");
        }
        this.prefix = prefix;
        this.code = code;
    }

    public static Ansi8BitColor of(int code) {
        return new Ansi8BitColor("38;5;", code);
    }

    @Override
    public String code() {
        return this.prefix + this.code;
    }

}
