package cool.scx.http.x.http2.hpack;

import cool.scx.codec.huffman.HuffmanCodec;
import cool.scx.collections.ArrayUtils;
import cool.scx.common.bit_array.BitArray;

//todo 实现不正确
public class HPACKHuffmanCodec {

    public static final HPACKHuffmanCodec HPACK_HUFFMAN_CODEC = new HPACKHuffmanCodec();

    private final HuffmanCodec<Character> huffmanCodec;

    private HPACKHuffmanCodec() {
        this.huffmanCodec = new HuffmanCodec<>(HPACKHuffmanTable.HPACK_HUFFMAN_TABLE);
    }

    public String decode(byte[] data) {
        var codePath = new BitArray(data);
        var chars = huffmanCodec.decode(codePath);
        var charArray = ArrayUtils.toPrimitive(chars.toArray(Character[]::new));
        return new String(charArray);
    }


}
