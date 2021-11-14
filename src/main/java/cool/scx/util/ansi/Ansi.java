package cool.scx.util.ansi;

import cool.scx.util.RandomUtils;

/**
 * 向控制台打印彩色
 *
 * @author scx567888
 * @version 1.0.10
 */
public final class Ansi {

    /**
     * 颜色列表
     */
    private static final AnsiColor[] printColor = AnsiColor.values();

    /**
     * 下一个颜色 做内部索引使用
     */
    private static int nextPrintColor = RandomUtils.getRandomNumber(0, printColor.length);

    /**
     * 待输出的数据
     */
    private final StringBuilder stringBuilder = new StringBuilder();

    /**
     * 私有构造函数
     */
    private Ansi() {
    }

    /**
     * <p>out.</p>
     *
     * @return a {@link cool.scx.util.ansi.Ansi} object
     */
    public static Ansi out() {
        return new Ansi();
    }

    /**
     * 红色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi red(Object o) {
        return add(o, AnsiColor.RED);
    }

    /**
     * 绿色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi green(Object o) {
        return add(o, AnsiColor.GREEN);
    }

    /**
     * 亮青色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi brightCyan(Object o) {
        return add(o, AnsiColor.BRIGHT_CYAN);
    }

    /**
     * 蓝色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi blue(Object o) {
        return add(o, AnsiColor.BLUE);
    }

    /**
     * 青色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi cyan(Object o) {
        return add(o, AnsiColor.CYAN);
    }

    /**
     * 亮蓝色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi brightBlue(Object o) {
        return add(o, AnsiColor.BRIGHT_BLUE);
    }

    /**
     * 亮紫色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi brightMagenta(Object o) {
        return add(o, AnsiColor.BRIGHT_MAGENTA);
    }

    /**
     * 亮红色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi brightRed(Object o) {
        return add(o, AnsiColor.BRIGHT_RED);
    }

    /**
     * 亮绿色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi brightGreen(Object o) {
        return add(o, AnsiColor.BRIGHT_GREEN);
    }

    /**
     * 黑色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi black(Object o) {
        return add(o, AnsiColor.BLACK);
    }

    /**
     * 亮黑色 ( 真的存在这种颜色吗 ? )
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi brightBlack(Object o) {
        return add(o, AnsiColor.BRIGHT_BLACK);
    }

    /**
     * 亮黄色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi brightYellow(Object o) {
        return add(o, AnsiColor.BRIGHT_YELLOW);
    }

    /**
     * 黄色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi yellow(Object o) {
        return add(o, AnsiColor.YELLOW);
    }

    /**
     * 紫色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi magenta(Object o) {
        return add(o, AnsiColor.MAGENTA);
    }

    /**
     * 白色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi white(Object o) {
        return add(o, AnsiColor.WHITE);
    }

    /**
     * 亮白色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi brightWhite(Object o) {
        return add(o, AnsiColor.BRIGHT_WHITE);
    }

    /**
     * 默认颜色
     *
     * @param o a {@link java.lang.Object} object.
     * @return Ansi 方便链式调用
     */
    public Ansi defaultColor(Object o) {
        return add(o, AnsiColor.DEFAULT_COLOR);
    }

    /**
     * 换行
     *
     * @return a {@link cool.scx.util.ansi.Ansi} object.
     */
    public Ansi ln() {
        stringBuilder.append(System.lineSeparator());
        return this;
    }

    /**
     * 向控制台打印输出 颜色根据内部计数器自动读取
     *
     * @param o a {@link java.lang.String} object.
     * @return a {@link cool.scx.util.ansi.Ansi} object.
     */
    public Ansi color(Object o) {
        if (nextPrintColor >= printColor.length) {
            nextPrintColor = 0;
        }
        add(o, printColor[nextPrintColor]);
        nextPrintColor = nextPrintColor + 1;
        return this;
    }

    /**
     * 添加
     *
     * @param o         o
     * @param ansiColor a
     * @return a
     */
    private Ansi add(Object o, AnsiColor ansiColor) {
        stringBuilder.append("\u001B[").append(ansiColor.code()).append("m").append(o).append("\u001B[0m");
        return this;
    }

    /**
     * <p>print.</p>
     */
    public void print() {
        System.err.print(stringBuilder);
    }

    /**
     * 清除当前的 所有 语句
     */
    public void clear() {
        stringBuilder.setLength(0);
    }

    /**
     * <p>println.</p>
     */
    public void println() {
        ln();
        print();
    }

}
