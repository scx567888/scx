package cool.scx.io.test;

import cool.scx.io.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;

public class DataReaderTest {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
    }

    @Test
    public static void test1() {
        var dataReader = new ByteArrayDataReader("11112345678".getBytes(StandardCharsets.UTF_8));

        //不会影响读取
        dataReader.peek(99);

        dataReader.indexOf("1".getBytes(StandardCharsets.UTF_8));

        var index = dataReader.readUntil("123".getBytes());

        try {
            //第二次应该匹配失败
            var index1 = dataReader.readUntil("123".getBytes());
        } catch (NoMatchFoundException _) {

        }
    }

    @Test
    public static void test2() {
        var sp = new ByteArrayDataSupplier(
                "1234567890".getBytes(),
                "abcdefghi".getBytes(),
                "jklmnopqrst".getBytes(),
                "uvwzyz".getBytes(),
                "ddf".getBytes(),
                "1".getBytes(),
                "3".getBytes(),
                "5".getBytes(),
                "7".getBytes(),
                "9".getBytes()
        );

        var dataReader = new LinkedDataReader(sp);

        Assert.assertEquals(dataReader.indexOf("1".getBytes()), 0);
        Assert.assertEquals(dataReader.indexOf("123456789".getBytes()), 0);
        Assert.assertEquals(dataReader.indexOf("789".getBytes()), 6);
        Assert.assertEquals(dataReader.indexOf("df".getBytes()), 37);
        Assert.assertEquals(dataReader.indexOf("ijklmnopqrstu".getBytes()), 18);
        try {
            //应该匹配失败
            dataReader.indexOf("?".getBytes());
            throw new AssertionError();
        } catch (NoMatchFoundException _) {

        }

        Assert.assertEquals(dataReader.indexOf("3579".getBytes()), 40);

    }

    @Test
    public static void test3() {
        //测试资源耗尽攻击
        var dataReader = new LinkedDataReader(() -> new DataNode("aaaaaa".getBytes()));
        //最大只搜索 100 字节
        Assert.assertThrows(NoMatchFoundException.class, () -> {
            byte[] bytes = dataReader.readUntil("\r\n".getBytes(), 100);
        });
    }

    @Test
    public static void test4() {
        var d1 = new ByteArrayDataSupplier("123456aaabbb".getBytes());
        var d2 = new ByteArrayDataSupplier("cccddd456789".getBytes());
        var dataReader = new LinkedDataReader(new SequenceDataSupplier(d1, d2));
        var read = dataReader.read(Integer.MAX_VALUE);
        Assert.assertEquals(new String(read), "123456aaabbbcccddd456789");
    }

    @Test
    public static void test5() {
        var d1 = new ByteArrayDataSupplier("123456aaabbb".getBytes());
        var d2 = new ByteArrayDataSupplier("cccddd456789".getBytes());
        var dataReader = new LinkedDataReader(new SequenceDataSupplier(d1, d2));
        var read = dataReader.indexOf("bbbccc".getBytes());
        Assert.assertEquals(read, 9);
    }


}
