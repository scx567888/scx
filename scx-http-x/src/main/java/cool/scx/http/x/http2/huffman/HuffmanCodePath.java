package cool.scx.http.x.http2.huffman;

import java.util.BitSet;
import java.util.List;

/**
 * bitset 不包含 长度信息 所以需要这个包装类
 *
 * @param bitSet
 * @param length
 */
public record HuffmanCodePath(BitSet bitSet, int length) {

    public static HuffmanCodePath concat(List<HuffmanCodePath> paths) {
        BitSet[] bitSets = new BitSet[paths.size()];
        int[] lengths = new int[paths.size()];
        int totalLength = 0;

        for (int i = 0; i < paths.size(); i++) {
            var n = paths.get(i);
            bitSets[i] = n.bitSet();
            lengths[i] = n.length();
            totalLength += lengths[i];
        }

        BitSet combined = concatenateBitSetsOptimized(bitSets, lengths);
        return new HuffmanCodePath(combined, totalLength);
    }

    public static HuffmanCodePath concat(HuffmanCodePath... paths) {
        BitSet[] bitSets = new BitSet[paths.length];
        int[] lengths = new int[paths.length];
        int totalLength = 0;

        for (int i = 0; i < paths.length; i++) {
            var n = paths[i];
            bitSets[i] = n.bitSet();
            lengths[i] = n.length();
            totalLength += lengths[i];
        }

        BitSet combined = concatenateBitSetsOptimized(bitSets, lengths);
        return new HuffmanCodePath(combined, totalLength);
    }

    private static BitSet concatenateBitSetsOptimized(BitSet[] bitSets, int[] lengths) {
        if (bitSets.length != lengths.length) {
            throw new IllegalArgumentException("bitSets and lengths must have the same size.");
        }

        // 计算总长度，初始化结果 BitSet 的 long 数组
        int totalLength = 0;
        for (int length : lengths) {
            totalLength += length;
        }

        BitSet result = new BitSet(totalLength);
        int currentIndex = 0;

        for (int i = 0; i < bitSets.length; i++) {
            BitSet bitSet = bitSets[i];
            int length = lengths[i];

            // 优化拷贝，直接从 bitSet 的 long[] 中读取并写入 result
            for (int j = 0; j < length; j++) {
                if (bitSet.get(j)) {
                    result.set(currentIndex);
                }
                currentIndex++;
            }
        }

        return result;
    }

    // 从字节数组和长度恢复 HuffmanCodePath
    public static HuffmanCodePath fromBytes(byte[] bytes) {
        // 使用 BitSet 的内置方法将字节数组转换为 BitSet
        BitSet bitSet = BitSet.valueOf(bytes);

        // 注意：需要提供长度信息确保 BitSet 不包含多余位
        return new HuffmanCodePath(bitSet, bytes.length*8);
    }

    // 从字节数组和长度恢复 HuffmanCodePath
    public static HuffmanCodePath fromBytes(byte[] bytes, int length) {
        // 使用 BitSet 的内置方法将字节数组转换为 BitSet
        BitSet bitSet = BitSet.valueOf(bytes);

        // 注意：需要提供长度信息确保 BitSet 不包含多余位
        return new HuffmanCodePath(bitSet, length);
    }

    // 静态方法：从二进制字符串创建
    public static HuffmanCodePath fromBinaryString(String binaryString) {
        BitSet bitSet = new BitSet();
        int bitIndex = 0; // 当前 BitSet 的索引
        // 遍历字符串
        for (char c : binaryString.toCharArray()) {
            switch (c) {
                case '1' -> {
                    bitSet.set(bitIndex); // 仅当字符是 '1' 时设置为 true
                    bitIndex++;
                }
                case '0' ->
                    // 字符是 '0'，索引前进（默认值为 false，无需显式设置）
                        bitIndex++;
                default -> {
                    // 其他字符（分隔符）直接跳过        
                }
            }
        }
        return new HuffmanCodePath(bitSet, bitIndex);
    }

    // 将路径转为二进制字符串
    public String toBinaryString() {
        var sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(bitSet.get(i) ? '1' : '0');
        }
        return sb.toString();
    }

    // 转换为紧凑的字节数组表示
    public byte[] toBytes() {
//        // 计算所需的字节长度 (向上取整)
//        int byteLength = (length + 7) / 8;
//        byte[] bytes = new byte[byteLength];
//
//        // 将位信息写入字节数组
//        for (int i = 0; i < length; i++) {
//            if (bitSet.get(i)) {
//                bytes[i / 8] |= (byte) (1 << (7 - (i % 8))); // 高位优先存储
//            }
//        }

        return bitSet.toByteArray();
    }

    @Override
    public String toString() {
        return "Path: " + toBinaryString() + " (Length: " + length + ")";
    }

}
