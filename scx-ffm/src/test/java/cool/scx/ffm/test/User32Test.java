package cool.scx.ffm.test;

import static cool.scx.ffm.platform.win32.User32.USER32;

public class User32Test {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        USER32.MessageBoxA(null, "测试中文内容", "😀😁😂测试标题", 0);
    }

}
