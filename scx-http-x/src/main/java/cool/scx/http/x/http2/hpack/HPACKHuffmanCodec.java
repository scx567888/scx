package cool.scx.http.x.http2.hpack;

import cool.scx.common.bit_array.BitArray;
import cool.scx.common.huffman.HuffmanCodec;
import cool.scx.common.util.ArrayUtils;

public class HPACKHuffmanCodec {

    public static final HPACKHuffmanCodec HPACK_HUFFMAN_CODEC = new HPACKHuffmanCodec();

    private final HuffmanCodec<Character> huffmanCodec;

    private HPACKHuffmanCodec() {
        this.huffmanCodec = new HuffmanCodec<>(HPACKHuffmanTable.HPACK_HUFFMAN_TABLE);
    }

    public String decode(byte[] data, int length) {
        var codePath = new BitArray(data);
        var chars = huffmanCodec.decode(codePath);
        var charArray = ArrayUtils.toPrimitive(chars.toArray(Character[]::new));
        return new String(charArray);
    }

    /**
     * 计算 HPACK 数据的有效位长度（包括必要的填充位）。
     */
    public static int calculateEffectiveBitsForHPACK(byte[] data) {
        int totalBits = data.length * 8; // 总比特数
        int paddingBits = 0;

        // 获取最后一个字节
        byte lastByte = data[data.length - 1];

        // 检查最后一个字节的填充位（连续的 1）
        for (int i = 0; i < 8; i++) {
            if ((lastByte & (1 << i)) != 0) { // 检查从低位到高位的每一位
                paddingBits++;
            } else {
                break; // 一旦遇到非 1，就结束检查
            }
        }

        return totalBits - paddingBits; // 有效位数 = 总位数 - 填充位数
    }

    

}
