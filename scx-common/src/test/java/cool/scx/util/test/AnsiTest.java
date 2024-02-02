package cool.scx.util.test;

import cool.scx.util.ansi.Ansi;
import org.testng.annotations.Test;

public class AnsiTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var ansi = Ansi.out();
        for (int i = 0; i < 99; i++) {
            ansi.color("■");
        }
        ansi.println();
    }

}
