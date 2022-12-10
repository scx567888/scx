package cool.scx.util.ansi;

import com.sun.jna.Function;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinNT.HANDLE;

import java.util.*;

import static com.sun.jna.platform.win32.WinDef.BOOL;
import static com.sun.jna.platform.win32.WinDef.DWORDByReference;

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
    private static final boolean enabled = detectIfAnsiCapable();

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
     * Windows 10 supports Ansi codes. However, it's still experimental and not enabled by default.
     * <br>
     * This method enables the necessary Windows 10 feature.
     * <br>
     * More info: <a href="https://stackoverflow.com/a/51681675/675577">...</a>
     * Code source: <a href="https://stackoverflow.com/a/52767586/675577">...</a>
     * Reported issue: <a href="https://github.com/PowerShell/PowerShell/issues/11449#issuecomment-569531747">...</a>
     */
    private static void enableWindows10AnsiSupport() {

        // See https://docs.microsoft.com/zh-cn/windows/console/getstdhandle
        var GetStdHandleFunc = Function.getFunction("kernel32", "GetStdHandle");
        var STD_OUTPUT_HANDLE = new DWORD(-11);
        var hOut = (HANDLE) GetStdHandleFunc.invoke(HANDLE.class, new Object[]{STD_OUTPUT_HANDLE});

        //See https://docs.microsoft.com/zh-cn/windows/console/getconsolemode
        var p_dwMode = new DWORDByReference(new DWORD(0));
        var GetConsoleModeFunc = Function.getFunction("kernel32", "GetConsoleMode");
        GetConsoleModeFunc.invoke(BOOL.class, new Object[]{hOut, p_dwMode});

        //See https://docs.microsoft.com/zh-cn/windows/console/setconsolemode
        int ENABLE_VIRTUAL_TERMINAL_PROCESSING = 4;
        var dwMode = p_dwMode.getValue();
        dwMode.setValue(dwMode.intValue() | ENABLE_VIRTUAL_TERMINAL_PROCESSING);
        var SetConsoleModeFunc = Function.getFunction("kernel32", "SetConsoleMode");
        SetConsoleModeFunc.invoke(BOOL.class, new Object[]{hOut, dwMode});

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
     * Encode a single {@link cool.scx.util.ansi.AnsiElement} if output is enabled.
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
     * Create a new ANSI string from the specified elements. Any {@link cool.scx.util.ansi.AnsiElement}s will
     * be encoded as required.
     *
     * @param elements the elements to encode
     * @return a string of the encoded elements
     */
    public static String toString(Object... elements) {
        StringBuilder sb = new StringBuilder();
        if (enabled) {
            buildEnabled(sb, elements);
        } else {
            buildDisabled(sb, elements);
        }
        return sb.toString();
    }

    /**
     * a
     *
     * @param sb       a
     * @param elements a
     */
    private static void buildEnabled(StringBuilder sb, Object[] elements) {
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
    private static void buildDisabled(StringBuilder sb, Object[] elements) {
        for (var element : elements) {
            if (!(element instanceof AnsiElement) && element != null) {
                sb.append(element);
            }
        }
    }

    /**
     * 检测是否支持 ansi
     *
     * @return ansi
     */
    private static boolean detectIfAnsiCapable() {
        String currentOS = System.getProperty("os.name");
        if (currentOS.startsWith("Windows")) {
            var winVersion = System.getProperty("os.version");
            if (winVersion.startsWith("10") || winVersion.startsWith("11")) {
                try {
                    enableWindows10AnsiSupport();
                    return true;
                } catch (Exception e) {
                    // 如果开启失败 则表示不支持
                    return false;
                }
            } else {// 不是 Windows 10 以上则表示不支持
                return false;
            }
        } else {// 不是 Windows 表示支持
            return true;
        }
    }

    /**
     * 过滤参数 每个类型的参数只留一种
     *
     * @param elements  a
     * @param elements2 b
     * @return an array of {@link cool.scx.util.ansi.AnsiElement} objects
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
     * @return a {@link cool.scx.util.ansi.Ansi} object.
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
     * @return a {@link cool.scx.util.ansi.Ansi} object.
     */
    public Ansi color(Object o, AnsiElement... ansiElements) {
        add(o, filterAnsiElement(ansiElements, CycleColors.next()));
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
        System.out.print(toString(this.elements.toArray()));
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

}
