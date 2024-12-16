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
        test5();
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
        for (int i = 0; i < 1001; i = i + 1) {
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
        for (int i = 0; i < 1001; i = i + 1) {
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

    @Test
    public static void test5() {
        byte[] array = {1, 2, 3, 4, 5};
        Assert.assertThrows(IndexOutOfBoundsException.class,()->ArrayUtils.subArray(array, -1, 10)); // 期望抛出异常

        int[] array1 = {1, 2, 3, 4, 5};
        int[] expected = {2, 3, 4};
        int[] result = ArrayUtils.subArray(array1, 1, 4);
        Assert.assertEquals(result, expected);

        long[] array2 = {1L, 2L, 3L, 4L, 5L};
        long[] expected2 = {};
        long[] result2 = ArrayUtils.safeSubArray(array2, -1, 10); // 期望返回空数组 
        Assert.assertEquals(result2, expected2);


        float[] array3 = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f}; 
        float[] expected3 = {2.0f, 3.0f};
        float[] result3 = ArrayUtils.safeSubArray(array3, 1, 3);
        Assert.assertEquals(result3, expected3);

        double[] array4 = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] subArrayResult4 = ArrayUtils.subArray(array4, 0, 3); 
        double[] safeSubArrayResult4 = ArrayUtils.safeSubArray(array4, 0, 3);
        Assert.assertEquals(subArrayResult4, safeSubArrayResult4);
    }

}
