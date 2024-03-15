package cool.scx.common.util.ansi;

import java.util.*;

/**
 * 向控制台打印彩色
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class Ansi {

    /**
     * 合并字符
     */
    private static final String ENCODE_JOIN = ";";

    /**
     * 起始字符
     */
    private static final String ENCODE_START = "\033[";

    /**
     * 结束字符
     */
    private static final String ENCODE_END = "m";

    /**
     * 重置
     */
    private static final String RESET = "0;" + AnsiColor.DEFAULT.code();

    /**
     * 是否开启颜色
     */
    private static final boolean enabled = AnsiHelper.detectIfAnsiCapable();

    /**
     * 循环颜色
     */
    private static final Iterator<AnsiColor> CYCLE_COLOR = AnsiHelper.initCycleColor();

    /**
     * 待输出的数据
     */
    private final List<Object> elements = new ArrayList<>();

    /**
     * 私有构造函数
     */
    private Ansi() {

    }

    /**
     * <p>out.</p>
     *
     * @return a {@link Ansi} object
     */
    public static Ansi out() {
        return new Ansi();
    }

    /**
     * Encode a single {@link AnsiElement} if output is enabled.
     *
     * @param element the element to encode
     * @return the encoded element or an empty string
     */
    public static String encode(AnsiElement element) {
        if (enabled) {
            return ENCODE_START + element + ENCODE_END;
        } else {
            return "";
        }
    }

    /**
     * a
     *
     * @param sb       a
     * @param elements a
     */
    private static void buildEnabled(StringBuilder sb, List<Object> elements) {
        boolean writingAnsi = false;
        boolean containsEncoding = false;
        for (var element : elements) {
            if (element instanceof AnsiElement) {
                containsEncoding = true;
                if (!writingAnsi) {
                    sb.append(ENCODE_START);
                    writingAnsi = true;
                } else {
                    sb.append(ENCODE_JOIN);
                }
                sb.append(((AnsiElement) element).code());
            } else {
                if (writingAnsi) {
                    sb.append(ENCODE_END);
                    writingAnsi = false;
                }
                sb.append(element);
            }
        }
        if (containsEncoding) {
            sb.append(writingAnsi ? ENCODE_JOIN : ENCODE_START);
            sb.append(RESET);
            sb.append(ENCODE_END);
        }
    }

    /**
     * a
     *
     * @param sb       a
     * @param elements a
     */
    private static void buildDisabled(StringBuilder sb, List<Object> elements) {
        for (var element : elements) {
            if (!(element instanceof AnsiElement) && element != null) {
                sb.append(element);
            }
        }
    }

    /**
     * 过滤参数 每个类型的参数只留一种
     *
     * @param elements  a
     * @param elements2 b
     * @return an array of {@link AnsiElement} objects
     */
    private static AnsiElement[] filterAnsiElement(AnsiElement[] elements, AnsiElement... elements2) {
        AnsiElement ansiColor = null;
        AnsiElement ansiBackground = null;
        Set<AnsiStyle> ansiStyleSet = new LinkedHashSet<>();

        var ansiElements = new AnsiElement[elements.length + elements2.length];
        System.arraycopy(elements, 0, ansiElements, 0, elements.length);
        System.arraycopy(elements2, 0, ansiElements, elements.length, elements2.length);

        for (var element : ansiElements) {
            if (element instanceof AnsiColor || element instanceof Ansi8BitColor) {
                ansiColor = element;
            } else if (element instanceof AnsiBackground || element instanceof Ansi8BitBackground) {
                ansiBackground = element;
            } else if (element instanceof AnsiStyle) {
                ansiStyleSet.add((AnsiStyle) element);
            }
        }
        var l = new ArrayList<AnsiElement>();
        if (ansiColor != null) {
            l.add(ansiColor);
        }
        if (ansiBackground != null) {
            l.add(ansiBackground);
        }
        l.addAll(ansiStyleSet);
        return l.toArray(AnsiElement[]::new);
    }

    /**
     * 红色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi red(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.RED));
    }

    /**
     * 绿色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi green(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.GREEN));
    }

    /**
     * 亮青色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi brightCyan(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.BRIGHT_CYAN));
    }

    /**
     * 蓝色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi blue(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.BLUE));
    }

    /**
     * 青色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi cyan(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.CYAN));
    }

    /**
     * 亮蓝色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi brightBlue(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.BRIGHT_BLUE));
    }

    /**
     * 亮紫色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi brightMagenta(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.BRIGHT_MAGENTA));
    }

    /**
     * 亮红色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi brightRed(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.BRIGHT_RED));
    }

    /**
     * 亮绿色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi brightGreen(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.BRIGHT_GREEN));
    }

    /**
     * 黑色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi black(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.BLACK));
    }

    /**
     * 亮黑色 ( 真的存在这种颜色吗 ? )
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi brightBlack(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.BRIGHT_BLACK));
    }

    /**
     * 亮黄色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi brightYellow(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.BRIGHT_YELLOW));
    }

    /**
     * 黄色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi yellow(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.YELLOW));
    }

    /**
     * 紫色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi magenta(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.MAGENTA));
    }

    /**
     * 白色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi white(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.WHITE));
    }

    /**
     * 亮白色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi brightWhite(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.BRIGHT_WHITE));
    }

    /**
     * 默认颜色
     *
     * @param o            a {@link java.lang.Object} object.
     * @param ansiElements AnsiElements
     * @return Ansi 方便链式调用
     */
    public Ansi defaultColor(Object o, AnsiElement... ansiElements) {
        return add(o, filterAnsiElement(ansiElements, AnsiColor.DEFAULT));
    }

    /**
     * 换行
     *
     * @return a {@link Ansi} object.
     */
    public Ansi ln() {
        add(System.lineSeparator());
        return this;
    }

    /**
     * 向控制台打印输出 颜色根据内部计数器自动读取
     *
     * @param o            a {@link java.lang.String} object.
     * @param ansiElements AnsiElements
     * @return a {@link Ansi} object.
     */
    public Ansi color(Object o, AnsiElement... ansiElements) {
        add(o, filterAnsiElement(ansiElements, CYCLE_COLOR.next()));
        return this;
    }

    /**
     * a
     *
     * @param o            a
     * @param ansiElements a
     * @return a
     */
    public Ansi add(Object o, AnsiElement... ansiElements) {
        elements.addAll(Arrays.asList(ansiElements));
        elements.add(o);
        return this;
    }

    /**
     * <p>print.</p>
     */
    public void print() {
        System.out.print(this);
    }

    public void print(boolean useAnsi) {
        System.out.print(this.toString(useAnsi));
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean useAnsi) {
        var sb = new StringBuilder();
        //系统支持 && 用户启用
        if (enabled && useAnsi) {
            buildEnabled(sb, elements);
        } else {
            buildDisabled(sb, elements);
        }
        return sb.toString();
    }

    /**
     * 清除当前的 所有 语句
     */
    public void clear() {
        elements.clear();
    }

    /**
     * <p>println.</p>
     */
    public void println() {
        ln();
        print();
    }

    public void println(boolean useAnsi) {
        ln();
        print(useAnsi);
    }

}
