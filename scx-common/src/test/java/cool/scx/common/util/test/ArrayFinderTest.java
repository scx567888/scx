package cool.scx.common.util.test;

import cool.scx.common.util.ArrayFinder;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ArrayFinderTest {

    public static void main(String[] args) throws Exception {
        test1();
    }

    public static void test1() {
        var s = new ArrayList<byte[]>();
        s.add("1234567890".getBytes());
        s.add("abcdefghi".getBytes());
        s.add("jklmnopqrst".getBytes());
        s.add("uvwzyz".getBytes());
        s.add("ddf".getBytes());
        s.add("1".getBytes());
        s.add("3".getBytes());
        s.add("5".getBytes());
        s.add("7".getBytes());
//        s.add("9".getBytes());
        var d = new AtomicInteger(0);
        Supplier<byte[]> sp = () -> {
            try {
                return s.get(d.getAndIncrement());
            } catch (Exception e) {
                return null;
            }
        };

        var ll = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
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

}
