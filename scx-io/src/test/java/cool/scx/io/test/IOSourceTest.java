package cool.scx.io.test;

import cool.scx.io.InputSource;
import cool.scx.io.input_source.ByteArrayInputSource;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class IOSourceTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    public static void test1() throws IOException {
        InputSource inputSource = new ByteArrayInputSource("测试文字".getBytes());
        var string = inputSource.readAll();
        System.out.println(new String(string));
    }

}
