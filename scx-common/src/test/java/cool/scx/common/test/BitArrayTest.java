package cool.scx.common.test;

import cool.scx.common.bit_array.BitArray;
import cool.scx.common.bit_array.CombinedBitArray;

public class BitArrayTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var a = new BitArray("abc".getBytes());
        var b = new BitArray("def".getBytes());
        var c = new CombinedBitArray(a, b);
        String s = new String(c.toBytes());
        System.out.println(s);
    }

}
