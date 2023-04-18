package cool.scx.utils.test;

import cool.scx.util.ArrayUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ArrayUtilsTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var a1 = new byte[]{1, 2, 3, 4, 5, 6};
        var a2 = new byte[]{3, 4, 5};
        var a3 = new byte[]{5, 8};
        Assert.assertEquals(ArrayUtils.indexOf(a1, a2), 2);
        Assert.assertEquals(ArrayUtils.indexOf(a1, a3), -1);
    }

}
