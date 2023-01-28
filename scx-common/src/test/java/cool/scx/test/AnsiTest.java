package cool.scx.test;

import cool.scx.util.ansi.Ansi;
import org.testng.annotations.Test;

public class AnsiTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        for (int i = 0; i < 99; i++) {
            Ansi.out().color(i).print();
        }
    }

}
