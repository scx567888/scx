package cool.scx.utils.test;

import cool.scx.util.ArrayUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static cool.scx.util.ArrayUtils.splitList;
import static cool.scx.util.ArrayUtils.splitListN;

public class ArrayUtilsTest {

    public static void main(String[] args) {
        test1();
        test2();
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

}
