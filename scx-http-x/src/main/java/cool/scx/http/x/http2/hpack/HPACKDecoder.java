package cool.scx.http.x.http2.hpack;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class HPACKDecoder {

    // 动态表
    private final List<String[]> dynamicTable = new ArrayList<>();

    // 最大动态表大小
    private static final int MAX_DYNAMIC_TABLE_SIZE = 4096;

    public Map<String, String> decode(byte[] data) {
        Map<String, String> headersMap = new HashMap<>();
        int index = 0;

        while (index < data.length) {
            int firstByte = data[index++] & 0xFF;

            if ((firstByte & 0x80) != 0) { // Indexed Header Field Representation
                int indexValue = firstByte & 0x7F;
                String[] header = getHeaderFromIndex(indexValue);
                headersMap.put(header[0], header[1]);
            } else if ((firstByte & 0x40) != 0) { // Literal Header Field with Incremental Indexing
                boolean huffmanEncoded = (firstByte & 0x20) != 0;
                String name = readStringLiteral(data, index, huffmanEncoded);
                index += name.length() + 1;
                huffmanEncoded = (data[index] & 0x80) != 0;
                String value = readStringLiteral(data, index, huffmanEncoded);
                index += value.length() + 1;

                headersMap.put(name, value);
                addToDynamicTable(new String[]{name, value});
            } else { // Literal Header Field without Indexing / never Indexed
                String name;
                if ((firstByte & 0x0F) == 0) {
                    boolean huffmanEncoded = (data[index] & 0x80) != 0;
                    name = readStringLiteral(data, index, huffmanEncoded);
                    index += name.length() + 1;
                } else {
                    int indexValue = firstByte & 0x0F;
                    name = getHeaderFromIndex(indexValue)[0];
                }
                boolean huffmanEncoded = (data[index] & 0x80) != 0;
                String value = readStringLiteral(data, index, huffmanEncoded);
                index += value.length() + 1;

                headersMap.put(name, value);
            }
        }

        return headersMap;
    }

    private String readStringLiteral(byte[] data, int index, boolean huffmanEncoded) {
        int length = data[index] & 0x7F;
        byte[] stringBytes = new byte[length];
        System.arraycopy(data, index + 1, stringBytes, 0, length);

        if (huffmanEncoded) {
            return Huffman.decode(stringBytes);
        } else {
            return new String(stringBytes);
        }
    }

    private void addToDynamicTable(String[] header) {
        dynamicTable.add(0, header);
        int tableSize = dynamicTable.stream().mapToInt(h -> h[0].length() + h[1].length() + 32).sum();

        while (tableSize > MAX_DYNAMIC_TABLE_SIZE) {
            dynamicTable.remove(dynamicTable.size() - 1);
            tableSize = dynamicTable.stream().mapToInt(h -> h[0].length() + h[1].length() + 32).sum();
        }
    }

    private String[] getHeaderFromIndex(int index) {
        if (index < 1 || index > HPACKStaticTable.HPACK_STATIC_TABLE.length) {
            throw new IllegalArgumentException("Invalid HPACK static table index: " + index);
        }
        if (index <= HPACKStaticTable.HPACK_STATIC_TABLE.length) {
            return HPACKStaticTable.ofIndex(index - 1);
        } else {
            return dynamicTable.get(index - HPACKStaticTable.HPACK_STATIC_TABLE.length - 1);
        }
    }
}

// 霍夫曼解码器的示例实现（实际实现可能更复杂）
class Huffman {
    public static String decode(byte[] encoded) {
        // 假设此方法实现了霍夫曼解码的逻辑
        return new String(encoded); // 这里仅为示例，实际实现需要解码逻辑
    }
}
