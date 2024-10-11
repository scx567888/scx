package cool.scx.io.test.t;


import org.testng.Assert;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ArrayFinderTest {

    public static void main(String[] args) throws Exception {
        test2();
    }

    public static void test1() {
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

        for (int j = 0; j < 200; j++) {
            var ll = System.nanoTime();
            for (int i = 0; i < 20000; i++) {
                ArrayFinder.indexOf(sp, "1".getBytes());
                d.set(0);
                ArrayFinder.indexOf(sp, "123456789".getBytes());
                d.set(0);
                ArrayFinder.indexOf(sp, "789".getBytes());
                d.set(0);
                ArrayFinder.indexOf(sp, "df".getBytes());
                d.set(0);
                ArrayFinder.indexOf(sp, "ijklmnopqrstu".getBytes());
                d.set(0);
                ArrayFinder.indexOf(sp, "?".getBytes());
                d.set(0);
                ArrayFinder.indexOf(sp, "3579".getBytes());
                d.set(0);
            }

            System.out.println((System.nanoTime() - ll) / 1000_000);
        }

        Assert.assertEquals(ArrayFinder.indexOf(sp, "1".getBytes()), 0);
        d.set(0);
        Assert.assertEquals(ArrayFinder.indexOf(sp, "123456789".getBytes()), 0);
        d.set(0);
        Assert.assertEquals(ArrayFinder.indexOf(sp, "789".getBytes()), 6);
        d.set(0);
        Assert.assertEquals(ArrayFinder.indexOf(sp, "df".getBytes()), 37);
        d.set(0);
        Assert.assertEquals(ArrayFinder.indexOf(sp, "ijklmnopqrstu".getBytes()), 18);
        d.set(0);
        Assert.assertEquals(ArrayFinder.indexOf(sp, "?".getBytes()), -1);
        d.set(0);
        Assert.assertEquals(ArrayFinder.indexOf(sp, "3579".getBytes()), 40);

    }

    public static void test2() throws IOException {
//        BufferedSearchInputStream s = new BufferedSearchInputStream(new ByteArrayInputStream("abcabcd".getBytes()));
//        int i = s.indexOf("abcd".getBytes());
//        System.out.println(i);
//        System.out.println(new String(s.readAllBytes()));
    }

}
