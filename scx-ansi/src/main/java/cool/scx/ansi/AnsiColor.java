package cool.scx.ansi;

/// ANSI 基本前景色
///
/// @author scx567888
/// @version 0.0.1
public enum AnsiColor implements AnsiElement {

    DEFAULT("39"),
    BLACK("30"),
    RED("31"),
    GREEN("32"),
    YELLOW("33"),
    BLUE("34"),
    MAGENTA("35"),
    CYAN("36"),
    WHITE("37"),
    BRIGHT_BLACK("90"),
    BRIGHT_RED("91"),
    BRIGHT_GREEN("92"),
    BRIGHT_YELLOW("93"),
    BRIGHT_BLUE("94"),
    BRIGHT_MAGENTA("95"),
    BRIGHT_CYAN("96"),
    BRIGHT_WHITE("97");

    private final String code;

    AnsiColor(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

}
