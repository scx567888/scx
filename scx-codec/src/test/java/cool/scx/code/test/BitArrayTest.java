package cool.scx.code.test;

import cool.scx.codec.bit_array.BitArray;
import cool.scx.codec.bit_array.BitArrayView;

public class BitArrayTest {

    public static void main(String[] args) {
        try {
            testAppendEmptyToNonEmpty();
            testAppendToEmpty();
            testAppendByteAligned();
            testAppendNonByteAligned();
            testAppendLargeBitArrays();
            testAppendToZeroLengthArray();
            testAppendAllOnesAndZeros();
            testAppendCrossByteBoundary();
            testAppendCrossByteBoundary2();
            testAppendSelf();
            testAppendNearMaxCapacity();
            testBitArrayView();

            System.out.println("所有测试用例都已通过！");
        } catch (AssertionError e) {
            System.err.println("测试失败: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void testAppendEmptyToNonEmpty() {
        BitArray bitArray1 = new BitArray("10101010");
        BitArray bitArrayEmpty = new BitArray();

        bitArray1.append(bitArrayEmpty);

        assert "10101010".equals(bitArray1.toBinaryString()) : "testAppendEmptyToNonEmpty: 拼接后内容不匹配";
        assert bitArray1.length() == 8 : "testAppendEmptyToNonEmpty: 长度不匹配";
    }

    public static void testAppendToEmpty() {
        BitArray bitArrayEmpty = new BitArray();
        BitArray bitArray2 = new BitArray("1100");

        bitArrayEmpty.append(bitArray2);

        assert "1100".equals(bitArrayEmpty.toBinaryString()) : "testAppendToEmpty: 拼接后内容不匹配";
        assert bitArrayEmpty.length() == 4 : "testAppendToEmpty: 长度不匹配";
    }

    public static void testAppendByteAligned() {
        BitArray bitArray1 = new BitArray("11110000"); // 8 bits
        BitArray bitArray2 = new BitArray("00111100"); // 8 bits

        bitArray1.append(bitArray2);

        assert "1111000000111100".equals(bitArray1.toBinaryString()) : "testAppendByteAligned: 拼接后内容不匹配";
        assert bitArray1.length() == 16 : "testAppendByteAligned: 长度不匹配";
    }

    public static void testAppendNonByteAligned() {
        BitArray bitArray1 = new BitArray("10101");      // 5 bits
        BitArray bitArray2 = new BitArray("110");        // 3 bits
        BitArray bitArray3 = new BitArray("1001101");    // 7 bits

        bitArray1.append(bitArray2); // After first append
        bitArray1.append(bitArray3); // After second append

        assert "101011101001101".equals(bitArray1.toBinaryString()) : "testAppendNonByteAligned: 拼接后内容不匹配";
        assert bitArray1.length() == 15 : "testAppendNonByteAligned: 长度不匹配";
    }

    public static void testAppendLargeBitArrays() {
        int size1 = 1024; // 1 kilobit
        int size2 = 2048; // 2 kilobits

        BitArray bitArray1 = new BitArray(size1);
        BitArray bitArray2 = new BitArray(size2);

        // Initialize the arrays with alternating bits
        for (int i = 0; i < size1; i = i + 1) {
            bitArray1.set(i, i % 2 == 0);
        }
        for (int i = 0; i < size2; i = i + 1) {
            bitArray2.set(i, i % 2 != 0);
        }

        bitArray1.append(bitArray2);

        assert bitArray1.length() == size1 + size2 : "testAppendLargeBitArrays: 长度不匹配";
        assert bitArray1.get(0) : "testAppendLargeBitArrays: 位0应为true";
        assert !bitArray1.get(size1) : "testAppendLargeBitArrays: 位" + size1 + "应为false";
    }

    public static void testAppendToZeroLengthArray() {
        BitArray bitArray1 = new BitArray(); // Zero length
        BitArray bitArray2 = new BitArray("1010101");

        bitArray1.append(bitArray2);

        assert "1010101".equals(bitArray1.toBinaryString()) : "testAppendToZeroLengthArray: 拼接后内容不匹配";
        assert bitArray1.length() == 7 : "testAppendToZeroLengthArray: 长度不匹配";
    }

    public static void testAppendAllOnesAndZeros() {
        BitArray allOnes = new BitArray("11111111");  // 8 bits set to 1
        BitArray allZeros = new BitArray("00000000"); // 8 bits set to 0

        allOnes.append(allZeros);

        assert "1111111100000000".equals(allOnes.toBinaryString()) : "testAppendAllOnesAndZeros: 拼接后内容不匹配";
        assert allOnes.length() == 16 : "testAppendAllOnesAndZeros: 长度不匹配";
    }

    public static void testAppendCrossByteBoundary() {
        BitArray bitArray1 = new BitArray("1111111"); // 7 bits
        BitArray bitArray2 = new BitArray("1");       // 1 bit

        bitArray1.append(bitArray2);

        assert "11111111".equals(bitArray1.toBinaryString()) : "testAppendCrossByteBoundary: 拼接后内容不匹配";
        assert bitArray1.length() == 8 : "testAppendCrossByteBoundary: 长度不匹配";
    }

    public static void testAppendCrossByteBoundary2() {
        BitArray bitArray1 = new BitArray("1111111");  // 7 bits
        BitArray bitArray2 = new BitArray("10000000"); // 8 bits

        bitArray1.append(bitArray2);

        String expected = "111111110000000"; // 前15位
        String actual = bitArray1.toBinaryString().substring(0, 15);

        assert expected.equals(actual) : "testAppendCrossByteBoundary2: 拼接后内容不匹配";
        assert bitArray1.length() == 15 : "testAppendCrossByteBoundary2: 长度不匹配";
    }

    public static void testAppendSelf() {
        BitArray bitArray = new BitArray("1010");

        bitArray.append(bitArray);

        assert "10101010".equals(bitArray.toBinaryString()) : "testAppendSelf: 拼接后内容不匹配";
        assert bitArray.length() == 8 : "testAppendSelf: 长度不匹配";
    }

    public static void testAppendNearMaxCapacity() {
        // 注意: 这个测试可能会消耗大量内存, 运行时请确保有足够的堆内存可用
        int nearMaxLength = Integer.MAX_VALUE / 8; // 防止内存溢出, 使用较小的值

        BitArray bitArray1 = new BitArray(nearMaxLength);
        BitArray bitArray2 = new BitArray(8);

        // 初始化最后几个比特位
        bitArray2.set(0, true);
        bitArray2.set(7, true);

        bitArray1.append(bitArray2);

        assert bitArray1.length() == nearMaxLength + 8 : "testAppendNearMaxCapacity: 长度不匹配";
        assert bitArray1.get(nearMaxLength) : "testAppendNearMaxCapacity: 位" + nearMaxLength + "应为true";
        assert bitArray1.get(nearMaxLength + 7) : "testAppendNearMaxCapacity: 位" + (nearMaxLength + 7) + "应为true";
    }

    public static void testBitArrayView() {
        BitArray bitArray1 = new BitArray("11001100");
        BitArray bitArray2 = new BitArray("00110011");
        BitArrayView bitArrayView = new BitArrayView(bitArray1, bitArray2);

        byte[] result = bitArrayView.toBytes();
        var binaryString = new StringBuilder();
        for (byte b : result) {
            binaryString.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }

        assert "1100110000110011".contentEquals(binaryString) : "testToBytes: 拼接后内容不匹配";
        assert bitArrayView.length() == 16 : "testToBytes: 长度不匹配";
    }

}
