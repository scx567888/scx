package cool.scx.ansi;

/**
 * 8Bit 前景色
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class Ansi8BitColor implements AnsiElement {

    private final int code;

    public Ansi8BitColor(int code) {
        if (code < 0 || code > 255) {
            throw new IllegalArgumentException("Code must be between 0 and 255");
        }
        this.code = code;
    }

    @Override
    public String code() {
        return "38;5;" + this.code;
    }

}
