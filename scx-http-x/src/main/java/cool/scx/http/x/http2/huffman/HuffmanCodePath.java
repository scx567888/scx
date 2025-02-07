package cool.scx.http.x.http2.huffman;

import java.util.BitSet;

/**
 * bitset 不包含 长度信息 所以需要这个包装类
 *
 * @param bitSet
 * @param length
 */
public record HuffmanCodePath(BitSet bitSet, int length) {

    // 将路径转为二进制字符串
    public String toBinaryString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(bitSet.get(i) ? '1' : '0');
        }
        return sb.toString();
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

    @Override
    public String toString() {
        return "Path: " + toBinaryString() + " (Length: " + length + ")";
    }

}
