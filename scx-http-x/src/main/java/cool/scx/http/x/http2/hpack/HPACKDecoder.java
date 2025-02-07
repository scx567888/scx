package cool.scx.http.x.http2.hpack;

import cool.scx.http.x.http2.huffman.HuffmanCodePath;
import cool.scx.http.x.http2.huffman.HuffmanCoding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HPACKDecoder {

    private final List<String[]> dynamicTable = new ArrayList<>();
    private static final int MAX_DYNAMIC_TABLE_SIZE = 4096;

    public Map<String, String> decode(byte[] data) {
        Map<String, String> headersMap = new HashMap<>();
        int index = 0;

        while (index < data.length) {
            int firstByte = data[index] & 0xFF;
            index += 1;

            if ((firstByte & 0x80) != 0) { // Indexed Header Field
                int[] result = decodeInteger(data, index - 1, 7);
                int headerIndex = result[0];
                index = result[1];
                String[] header = getHeaderFromIndex(headerIndex);
                headersMap.put(header[0], header[1]);
            } else if ((firstByte & 0x40) != 0) { // Literal with Incremental Indexing
                processLiteralHeader(data, firstByte, 6, headersMap, true, index - 1);
                index = processLiteralHeader(data, firstByte, 6, headersMap, true, index - 1);
            } else if ((firstByte & 0xF0) == 0x00) { // Literal without Indexing
                index = processLiteralHeader(data, firstByte, 4, headersMap, false, index - 1);
            } else if ((firstByte & 0xF0) == 0x10) { // Never Indexed
                index = processLiteralHeader(data, firstByte, 4, headersMap, false, index - 1);
            } else {
                throw new IllegalArgumentException("Unsupported header field type");
            }
        }
        return headersMap;
    }

    private int processLiteralHeader(byte[] data, int firstByte, int prefixBits,
                                     Map<String, String> headers, boolean addToTable, int startIndex) {
        int[] nameIndexResult = decodeInteger(data, startIndex, prefixBits);
        int nameIndex = nameIndexResult[0];
        int index = nameIndexResult[1];
        String name;

        if (nameIndex == 0) { // Literal name
            boolean huffmanName = (data[index] & 0x80) != 0;
            int[] nameLenResult = decodeInteger(data, index, 7);
            int nameLen = nameLenResult[0];
            index = nameLenResult[1];
            name = decodeString(data, index, nameLen, huffmanName);
            index += nameLen;
        } else { // Indexed name
            String[] header = getHeaderFromIndex(nameIndex);
            name = header[0];
        }

        // Read value
        boolean huffmanValue = (data[index] & 0x80) != 0;
        int[] valueLenResult = decodeInteger(data, index, 7);
        int valueLen = valueLenResult[0];
        index = valueLenResult[1];
        String value = decodeString(data, index, valueLen, huffmanValue);
        index += valueLen;

        headers.put(name, value);
        if (addToTable) {
            addToDynamicTable(new String[]{name, value});
        }
        return index;
    }

    private static int[] decodeInteger(byte[] data, int startIndex, int prefixBits) {
        int prefixMask = (1 << prefixBits) - 1;
        int index = startIndex;
        int value = data[index] & prefixMask;
        index += 1;

        if (value == prefixMask) {
            int m = 0;
            int b;
            do {
                b = data[index] & 0xFF;
                value += (b & 0x7F) << m;
                m += 7;
                index += 1;
            } while ((b & 0x80) != 0);
        }
        return new int[]{value, index};
    }

    private String decodeString(byte[] data, int start, int length, boolean huffman) {
        byte[] bytes = new byte[length];
        System.arraycopy(data, start, bytes, 0, length);
        return huffman ? Huffman.decode(bytes) : new String(bytes);
    }

    private void addToDynamicTable(String[] header) {
        dynamicTable.add(0, header);
        int entrySize = header[0].length() + header[1].length() + 32;
        int currentSize = dynamicTable.stream().mapToInt(e -> e[0].length() + e[1].length() + 32).sum();

        while (currentSize > MAX_DYNAMIC_TABLE_SIZE) {
            String[] removed = dynamicTable.remove(dynamicTable.size() - 1);
            currentSize -= removed[0].length() + removed[1].length() + 32;
        }
    }

    private String[] getHeaderFromIndex(int index) {
        if (index <= HPACKStaticTable.STATIC_TABLE.size()) {
            return HPACKStaticTable.get(index);
        } else {
            int dynamicIndex = index - HPACKStaticTable.STATIC_TABLE.size() - 1;
            if (dynamicIndex < 0 || dynamicIndex >= dynamicTable.size()) {
                throw new IllegalArgumentException("Invalid header index: " + index);
            }
            return dynamicTable.get(dynamicIndex);
        }
    }

    

    static class Huffman {
        static String decode(byte[] data) {
            var huffmanCoding = new HuffmanCoding<Byte>(HPACKHuffmanTable.HPACK_HUFFMAN_TABLE);
            HuffmanCodePath huffmanCodePath = HuffmanCodePath.fromBytes(data);
            var ddd=huffmanCodePath.toBytes();
            var decode = huffmanCoding.decode(HuffmanCodePath.fromBytes(data), Byte.class);
            //todo 
            System.out.println(decode);
//             需要ai 来写
//            return new String(decode);
            return null;
        }
    }
}
