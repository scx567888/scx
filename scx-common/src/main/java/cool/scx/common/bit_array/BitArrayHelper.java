package cool.scx.common.bit_array;

public class BitArrayHelper {

    // 掩码静态查找表
    public static final byte[] BIT_MASKS = {
            (byte) 0x80, (byte) 0x40, (byte) 0x20, (byte) 0x10,
            (byte) 0x08, (byte) 0x04, (byte) 0x02, (byte) 0x01
    };

    public static int byteIndex(int index) {
        return index >> 3;
    }

    public static int bitIndex(int index) {
        return index & 7;
    }

    public static int byteLength(int bitLength) {
        return (bitLength + 7) >> 3; // 向上取整，计算最小字节数
    }

    public static int byteCapacity(byte[] bytes) {
        return bytes.length << 3;
    }

    public static void setByBinaryString(BitArray bitArray, String binaryString) {
        // 遍历字符串
        for (var c : binaryString.toCharArray()) {
            // 其他字符（分隔符）直接跳过        
            switch (c) {
                case '1' -> bitArray.append(true); // 仅当字符是 '1' 时设置为 true
                case '0' -> bitArray.append(false); // 仅当字符是 '1' 时设置为 true
            }
        }
    }

}
