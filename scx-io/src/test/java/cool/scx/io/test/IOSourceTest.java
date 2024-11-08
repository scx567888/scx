package cool.scx.io.test;

import cool.scx.io.InputSource;
import cool.scx.io.input_source.ByteArrayInputSource;

import java.io.IOException;

public class IOSourceTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    public static void test1() throws IOException {
        InputSource inputSource = new ByteArrayInputSource("测试文字".getBytes());
        var string = inputSource.readAll();
        var string1 = inputSource.readAll();
        System.out.println(new String(string));
        System.out.println(string1);//应该是 null
    }

}
