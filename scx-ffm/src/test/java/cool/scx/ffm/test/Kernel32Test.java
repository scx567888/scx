package cool.scx.ffm.test;

import cool.scx.ffm.type.mapper.IntMapper;

import static cool.scx.ffm.platform.win32.Kernel32.KERNEL32;

public class Kernel32Test {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var a = KERNEL32.GetStdHandle(-11);

        var i = new IntMapper();

        var b = KERNEL32.GetConsoleMode(a, i);

        var c = KERNEL32.SetConsoleMode(a, i.getValue());
    }

}
