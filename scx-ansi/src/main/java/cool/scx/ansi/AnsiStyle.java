package cool.scx.ansi;

/**
 * Ansi 样式
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum AnsiStyle implements AnsiElement {

    RESET("0"),              // 重置所有样式
    BOLD("1"),               // 粗体
    FAINT("2"),              // 微弱
    ITALIC("3"),             // 斜体
    UNDERLINE("4"),          // 下划线
    BLINK("5"),              // 闪烁
    REVERSE("7"),            // 反转前景色和背景色
    HIDDEN("8"),             // 隐藏文本
    CROSSED_OUT("9"),        // 删除线
    DOUBLE_UNDERLINE("21"),  // 双下划线
    OVERLINE("53");          // 上划线

    private final String code;

    AnsiStyle(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

}
