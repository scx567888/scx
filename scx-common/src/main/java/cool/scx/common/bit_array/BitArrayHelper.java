package cool.scx.common.bit_array;

public class BitArrayHelper {

    public static void setByBinaryString(IBitArray bitArray, String binaryString) {
        int bitIndex = 0; // 当前 BitSet 的索引
        // 遍历字符串
        for (var c : binaryString.toCharArray()) {
            switch (c) {
                case '1' -> {
                    bitArray.set(bitIndex, true); // 仅当字符是 '1' 时设置为 true
                    bitIndex++;
                }
                case '0' -> {
                    bitArray.set(bitIndex, false);
                    bitIndex++;
                } // 仅当字符是 '1' 时设置为 true
                default -> {
                    // 其他字符（分隔符）直接跳过        
                }
            }
        }
    }

}
