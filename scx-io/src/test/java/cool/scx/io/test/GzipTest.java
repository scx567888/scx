package cool.scx.io.test;

import cool.scx.io.zip.GunzipBuilder;
import cool.scx.io.zip.GzipBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GzipTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    @Test
    public static void test1() throws IOException {
        var s = "Data";
        for (int i = 0; i < 10; i = i + 1) {
            s += s;
        }
        var b = s.getBytes();
        for (int i = 0; i < 10; i = i + 1) {
            b = new GzipBuilder(new ByteArrayInputStream(b)).readAllBytes();
            b = new GunzipBuilder(new ByteArrayInputStream(b)).readAllBytes();
        }
        Assert.assertEquals(s, new String(b));
    }

}
