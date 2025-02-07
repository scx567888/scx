package cool.scx.http.x.http2.huffman;

import java.util.*;


public class HuffmanCoding {

    private final Map<Byte, String> huffmanCodes;
    private final Map<String, Byte> reverseHuffmanCodes;

    // 构造函数接收字节数组，构建霍夫曼树
    public HuffmanCoding(byte[] input) {
        // 构建频率表
        Map<Byte, Integer> frequencyMap = new HashMap<>();
        for (byte b : input) {
            frequencyMap.put(b, frequencyMap.getOrDefault(b, 0) + 1);
        }

        // 构建优先队列
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.frequency));
        for (Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
            pq.offer(new HuffmanNode(entry.getValue(), entry.getKey()));
        }

        // 构建霍夫曼树
        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode node = new HuffmanNode(left.frequency + right.frequency, (byte) 0, left, right);
            pq.offer(node);
        }
        var root = pq.poll();

        // 生成霍夫曼编码
        huffmanCodes = new HashMap<>();
        reverseHuffmanCodes = new HashMap<>();
        buildHuffmanCodes(root, "");
    }

    // 构造函数接收预定义的霍夫曼树
    public HuffmanCoding(Map<Byte, String> predefinedHuffmanCodes) {
        this.huffmanCodes = new HashMap<>(predefinedHuffmanCodes);
        this.reverseHuffmanCodes = new HashMap<>();
        for (Map.Entry<Byte, String> entry : predefinedHuffmanCodes.entrySet()) {
            reverseHuffmanCodes.put(entry.getValue(), entry.getKey());
        }
    }

    private void buildHuffmanCodes(HuffmanNode node, String code) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            huffmanCodes.put(node.data, code);
            reverseHuffmanCodes.put(code, node.data);
        }
        buildHuffmanCodes(node.left, code + "0");
        buildHuffmanCodes(node.right, code + "1");
    }

    public byte[] encode(byte[] input) {
        StringBuilder encoded = new StringBuilder();
        for (byte b : input) {
            encoded.append(huffmanCodes.get(b));
        }
        int length = (encoded.length() + 7) / 8;
        byte[] result = new byte[length];
        for (int i = 0; i < encoded.length(); i++) {
            if (encoded.charAt(i) == '1') {
                result[i / 8] |= (128 >> (i % 8));
            }
        }
        return result;
    }

    public byte[] decode(byte[] encoded) {
        StringBuilder decodedBits = new StringBuilder();
        for (byte b : encoded) {
            for (int i = 0; i < 8; i++) {
                decodedBits.append((b & (128 >> i)) == 0 ? '0' : '1');
            }
        }
        List<Byte> result = new ArrayList<>();
        String code = "";
        for (int i = 0; i < decodedBits.length(); i++) {
            code += decodedBits.charAt(i);
            if (reverseHuffmanCodes.containsKey(code)) {
                result.add(reverseHuffmanCodes.get(code));
                code = "";
            }
        }
        byte[] resultArray = new byte[result.size()];
        for (int i = 0; i < result.size(); i++) {
            resultArray[i] = result.get(i);
        }
        return resultArray;
    }

}
