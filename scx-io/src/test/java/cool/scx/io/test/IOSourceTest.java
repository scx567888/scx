package cool.scx.io.test;

import cool.scx.io.InputSource;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class IOSourceTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    public static void test1() throws IOException {
        InputSource inputSource = InputSource.of("测试文字".getBytes());
        String string = inputSource.toString(UTF_8);
        System.out.println(string);
    }

}
