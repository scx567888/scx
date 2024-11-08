package cool.scx.io.test;

import cool.scx.io.gzip.GunzipInputSource;
import cool.scx.io.gzip.GzipInputSource;
import cool.scx.io.input_source.ByteArrayInputSource;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class GzipTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    @Test
    public static void test1() throws IOException {
        var s = "Data";
        for (int i = 0; i < 10; i++) {
            s += s;
        }
        var b = s.getBytes();
        for (int i = 0; i < 10; i++) {
            b = new GzipInputSource(new ByteArrayInputSource(b)).readAll();
            b = new GunzipInputSource(new ByteArrayInputSource(b)).readAll();
        }
        Assert.assertEquals(s, new String(b));
    }

}
