package cool.scx.io.test;

import cool.scx.io.ByteArrayDataReader;
import cool.scx.io.LinkedDataReader;
import cool.scx.io.NoMatchFoundException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class DataReaderTest {

    public static void main(String[] args) {
        test1();
        test2();
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
        var s = new byte[10][];
        s[0] = "1234567890".getBytes();
        s[1] = "abcdefghi".getBytes();
        s[2] = "jklmnopqrst".getBytes();
        s[3] = "uvwzyz".getBytes();
        s[4] = "ddf".getBytes();
        s[5] = "1".getBytes();
        s[6] = "3".getBytes();
        s[7] = "5".getBytes();
        s[8] = "7".getBytes();
        s[9] = "9".getBytes();
        var d = new AtomicInteger(0);
        Supplier<byte[]> sp = () -> {
            try {
                return s[d.getAndIncrement()];
            } catch (Exception e) {
                return null;
            }
        };

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


}
