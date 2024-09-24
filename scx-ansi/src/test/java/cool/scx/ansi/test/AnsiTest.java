package cool.scx.ansi.test;

import cool.scx.ansi.*;
import org.testng.annotations.Test;

public class AnsiTest {

    public static void main(String[] args) {
        test1();
        test2();
    }

    @Test
    public static void test1() {
        var ansi = Ansi.ansi();
        for (var color : AnsiColor.values()) {
            for (var ansiBackground : AnsiBackground.values()) {
                ansi.add("abc", color, ansiBackground);
            }
            ansi.ln();
        }
        ansi.println();
    }

    @Test
    public static void test2() {
        var ansi = Ansi.ansi();
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                ansi.add("0", new Ansi8BitColor(i), new Ansi8BitBackground(j), AnsiStyle.BOLD, AnsiStyle.ITALIC, AnsiStyle.UNDERLINE);
            }
            ansi.ln();
        }
        ansi.println();
    }

}
