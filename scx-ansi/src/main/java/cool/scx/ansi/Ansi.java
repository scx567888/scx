package cool.scx.ansi;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static cool.scx.ansi.AnsiColor.*;
import static cool.scx.common.util.ArrayUtils.concat;

/**
 * 向控制台打印彩色
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class Ansi {

    // 是否启用 ANSI 支持
    private static final boolean ENABLED = AnsiHelper.detectIfAnsiCapable();

    private final List<AnsiItem> items;

    public Ansi() {
        this.items = new ArrayList<>();
    }

    public static Ansi ansi() {
        return new Ansi();
    }

    /**
     * 过滤参数 每个类型的参数只留一种
     *
     * @param elements a
     * @return an array of {@link AnsiElement} objects
     */
    private static AnsiElement[] filterAnsiElement(AnsiElement... elements) {
        if (elements.length == 0) {
            return elements;
        }

        AnsiElement ansiColor = null;
        AnsiElement ansiBackground = null;
        var ansiStyleSet = new LinkedHashSet<AnsiStyle>();

        for (var element : elements) {
            if (element instanceof AnsiColor || element instanceof Ansi8BitColor) {
                ansiColor = element;
            } else if (element instanceof AnsiBackground || element instanceof Ansi8BitBackground) {
                ansiBackground = element;
            } else if (element instanceof AnsiStyle ansiStyle) {
                ansiStyleSet.add(ansiStyle);
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

    public Ansi add(Object o, AnsiElement... ansiElements) {
        items.add(new AnsiItem(o, filterAnsiElement(ansiElements)));
        return this;
    }

    public Ansi defaultColor(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, DEFAULT));
    }

    public Ansi black(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, BLACK));
    }

    public Ansi red(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, RED));
    }

    public Ansi green(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, GREEN));
    }

    public Ansi yellow(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, YELLOW));
    }

    public Ansi blue(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, BLUE));
    }

    public Ansi magenta(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, MAGENTA));
    }

    public Ansi cyan(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, CYAN));
    }

    public Ansi white(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, WHITE));
    }

    public Ansi brightBlack(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, BRIGHT_BLACK));
    }

    public Ansi brightRed(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, BRIGHT_RED));
    }

    public Ansi brightGreen(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, BRIGHT_GREEN));
    }

    public Ansi brightYellow(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, BRIGHT_YELLOW));
    }

    public Ansi brightBlue(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, BRIGHT_BLUE));
    }

    public Ansi brightMagenta(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, BRIGHT_MAGENTA));
    }

    public Ansi brightCyan(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, BRIGHT_CYAN));
    }

    public Ansi brightWhite(Object o, AnsiElement... ansiElements) {
        return add(o, concat(ansiElements, BRIGHT_WHITE));
    }

    public Ansi ln() {
        return add(System.lineSeparator());
    }

    public void print(boolean useAnsi) {
        System.out.print(this.toString(useAnsi));
    }

    public void print() {
        print(true);
    }

    public void println(boolean useAnsi) {
        ln().print(useAnsi);
    }

    public void println() {
        println(true);
    }

    public String toString(boolean useAnsi) {
        var sb = new StringBuilder();
        //系统支持 && 用户启用
        if (ENABLED && useAnsi) {
            for (var i : items) {
                i.buildEnabled(sb);
            }
        } else {
            for (var i : items) {
                i.buildDisabled(sb);
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(true);
    }

}
