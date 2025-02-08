package cool.scx.common.test;

import cool.scx.common.bit_array.BitArray;
import cool.scx.common.bit_array.CombinedBitArray;

public class BitArrayTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var ba = new BitArray();
        ba.set(0, true);
        ba.set(1, true);
        ba.set(2, true);
        ba.set(3, true);
        var ba1 = new BitArray();
        ba1.set(0, false);
        ba1.set(1, false);
        ba1.set(2, false);
        ba1.set(3, false);
        var ba2 = new CombinedBitArray(ba, ba1);
        for (int i = 0; i < 100000; i++) {
            byte[] bytes = ba2.toBytes();
        }
        System.out.println();
    }

}
