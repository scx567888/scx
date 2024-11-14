package cool.scx.common.test;

import cool.scx.common.util.ArrayUtils;
import cool.scx.common.util.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static cool.scx.common.util.ArrayUtils.*;

public class ArrayUtilsTest {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
    }

    @Test
    public static void test1() {
        var a1 = new byte[]{1, 2, 3, 4, 5, 6};
        var a2 = new byte[]{3, 4, 5};
        var a3 = new byte[]{5, 8};
        Assert.assertEquals(ArrayUtils.indexOf(a1, a2), 2);
        Assert.assertEquals(ArrayUtils.indexOf(a1, a3), -1);
    }

    @Test
    public static void test2() {
        var s = new ArrayList<>();
        for (int i = 0; i < 1001; i++) {
            s.add(i);
        }
        var lists1 = splitListN(s, 9);
        var lists2 = splitList(s, 9);
        Assert.assertEquals(lists1.get(0).size(), 112);
        Assert.assertEquals(lists2.get(lists2.size() - 1).size(), 2);
    }

    @Test
    public static void test3() {
        var s = new int[1001];
        for (int i = 0; i < 1001; i++) {
            s[i] = i;
        }
        var lists1 = splitArrayN(s, 9);
        var lists2 = splitArray(s, 9);
        Assert.assertEquals(lists1[0].length, 112);
        Assert.assertEquals(lists2[lists2.length - 1].length, 2);
    }

    @Test
    public static void test4() {
        var bytes = RandomUtils.randomBytes(101);
        var newBytes = Arrays.copyOf(bytes, bytes.length);
        ArrayUtils.reverse(newBytes);
        ArrayUtils.reverse(newBytes);
        Assert.assertEquals(newBytes, bytes);
    }

}
