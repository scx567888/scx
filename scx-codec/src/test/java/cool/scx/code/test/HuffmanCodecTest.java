package cool.scx.code.test;

import cool.scx.codec.huffman.HuffmanCodec;
import cool.scx.collections.bit_array.IBitArray;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class HuffmanCodecTest {

    public static void main(String[] args) {
        testBuildHuffmanCodecFromData();
        testEncodeSingleSymbol();
        testEncodeAndDecodeSingleSymbol();
        testEncodeAndDecodeList();
        testBuildHuffmanCodecFromCode();
        testToString();
    }

    @Test
    public static void testBuildHuffmanCodecFromData() {
        String[] data = {"a", "b", "a", "c", "a", "b", "a", "d"};
        HuffmanCodec<String> codec = new HuffmanCodec<>(data);
        assertNotNull(codec);
    }

    @Test
    public static void testEncodeSingleSymbol() {
        String[] data = {"a", "b", "a", "c", "a", "b", "a", "d"};
        HuffmanCodec<String> codec = new HuffmanCodec<>(data);
        IBitArray encoded = codec.encode("a");
        assertNotNull(encoded);
    }

    @Test
    public static void testEncodeAndDecodeSingleSymbol() {
        String[] data = {"a", "b", "a", "c", "a", "b", "a", "d"};
        HuffmanCodec<String> codec = new HuffmanCodec<>(data);
        IBitArray encoded = codec.encode("a");
        String decoded = codec.decodeSingle(encoded);
        assertEquals("a", decoded);
    }

    @Test
    public static void testEncodeAndDecodeList() {
        String[] data = {"a", "b", "a", "c", "a", "b", "a", "d"};
        HuffmanCodec<String> codec = new HuffmanCodec<>(data);
        List<String> symbols = Arrays.asList("a", "b", "c", "d");
        IBitArray encoded = codec.encode(symbols);
        List<String> decoded = codec.decode(encoded);
        assertEquals(symbols, decoded);
    }

    @Test
    public static void testBuildHuffmanCodecFromCode() {
        Map<String, String> codeMap = Map.of(
                "a", "0",
                "b", "10",
                "c", "110",
                "d", "111"
        );
        HuffmanCodec<String> codec = new HuffmanCodec<>(codeMap);
        assertNotNull(codec);
    }

    @Test
    public static void testToString() {
        String[] data = {"a", "b", "a", "c", "a", "b", "a", "d"};
        HuffmanCodec<String> codec = new HuffmanCodec<>(data);
        String codecString = codec.toString();
        assertNotNull(codecString);
    }

}
