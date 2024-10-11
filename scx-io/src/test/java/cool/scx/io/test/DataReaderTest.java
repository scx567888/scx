package cool.scx.io.test;

import cool.scx.io.ArrayDataReader;

import java.nio.charset.StandardCharsets;

public class DataReaderTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var dataReader = new ArrayDataReader("11112345678".getBytes(StandardCharsets.UTF_8));

        var index = dataReader.readMatch("123".getBytes());
        System.out.println(new String(index));

        //第二次应该匹配失败
        var index1 = dataReader.readMatch("123".getBytes());
        System.out.println(new String(index));

    }


}
