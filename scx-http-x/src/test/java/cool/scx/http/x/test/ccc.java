package cool.scx.http.x.test;

import cool.scx.common.bit_array.BitArray;
import cool.scx.common.huffman.HuffmanCodec;
import cool.scx.common.huffman.HuffmanHelper;
import cool.scx.http.x.http2.hpack.HPACKHuffmanTable;
import cool.scx.http.x.http2.hpack.HPACKUtils;

import java.util.List;
import java.util.stream.Collectors;

import static cool.scx.http.x.http2.hpack.HPACKHuffmanCodec.calculateEffectiveBitsForHPACK;

public class ccc {
    public static void main(String[] args) {
        // 示例 HPACK 霍夫曼编码数据（包含填充位）
//        byte[] data = {-96, -28, 29, 19, -99, 9, -72, -13, -49, -65};
        // 构造 BitArray（假设 BitArray 的构造函数接受字节数组，并且内部以 8 位为单位展开）
        // 剔除填充位，创建一个只包含有效位的视图
        BitArray avafdv = new BitArray("1111111000111111111100101010111111000100001|11111111|11111111|11111111|111111");

        // 使用静态表构造 HuffmanCodec 对象
        // 注意：静态表中的 key 为 Character，对应 HPACK 静态霍夫曼编码表
        HuffmanCodec<Character> huffmanCodec = new HuffmanCodec<>(HPACKHuffmanTable.HPACK_HUFFMAN_TABLE);

        List<Character> decode = huffmanCodec.decode(avafdv);
        System.out.println();

//        int i1 = calculateEffectiveBitsForHPACK(data);
//        int validBitLength = HPACKUtils.getValidBitLength(data);
//        int totalBits = data.length * 8;  // 总位数
//
//        // 根据 HPACK 规定，最后一个字节中未用的位（低位）必须全部为 1
//        // 我们通过检查最后一个字节，统计连续的填充位数
//        int lastByte = data[data.length - 1] & 0xFF;
//        int paddingCount = 0;
//        for (int i = 0; i < 8; i++) {
//            // 从最低位开始检查，遇到第一个 0 停止
//            if (((lastByte >> i) & 1) == 1) {
//                paddingCount++;
//            } else {
//                break;
//            }
//        }
//        int effectiveBits = totalBits - paddingCount;
//        System.out.println("总位数: " + totalBits + ", 填充位数: " + paddingCount + ", 有效位数: " + effectiveBits);
//
//        // 构造 BitArray（假设 BitArray 的构造函数接受字节数组，并且内部以 8 位为单位展开）
//        // 剔除填充位，创建一个只包含有效位的视图
//        BitArray trimmedBitArray = new BitArray(data, effectiveBits);
//
//        // 使用静态表构造 HuffmanCodec 对象
//        // 注意：静态表中的 key 为 Character，对应 HPACK 静态霍夫曼编码表
//        
//
//        // 调用解码器对有效位流进行解码
//        List<Character> decodedChars = huffmanCodec.decode(trimmedBitArray);
//        String decodedString = decodedChars.stream()
//                .map(String::valueOf)
//                .collect(Collectors.joining());
//
//        System.out.println("解码后的字符串: " + decodedString);
    }
}
