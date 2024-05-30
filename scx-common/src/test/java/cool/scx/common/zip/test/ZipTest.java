package cool.scx.common.zip.test;

import cool.scx.common.zip.GunzipBuilder;
import cool.scx.common.zip.GzipBuilder;
import cool.scx.common.zip.ZipBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;

public class ZipTest {

    public static void main(String[] args) throws Exception {
        test1();
        test2();
    }

    @Test
    public static void test1() throws Exception {
        var v = new ZipBuilder();
        v.put("\\空目录\\");
        v.put("///根目录\\scx.txt/", "scxscxscxscx".getBytes(StandardCharsets.UTF_8));
        v.put("根目录\\scx123.txt", "scxscxscxscx".getBytes(StandardCharsets.UTF_8));
        v.put("根目录\\第二层目录\\第三层目录\\\\\\\\\\文件.txt", "文件文件文件".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(615, v.toBytes().length);
    }

    @Test
    public static void test2() throws Exception {
        var s = "Data";
        for (int i = 0; i < 10; i++) {
            s += s;
        }
        var b = s.getBytes();
        for (int i = 0; i < 10; i++) {
            b = new GzipBuilder(b).toBytes();
            b = new GunzipBuilder(b).toBytes();
        }
        Assert.assertEquals(s, new String(b));
    }

}
