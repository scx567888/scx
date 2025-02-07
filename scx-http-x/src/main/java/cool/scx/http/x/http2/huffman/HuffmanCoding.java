package cool.scx.http.x.http2.huffman;

import java.util.ArrayList;
import java.util.Map;

import static cool.scx.http.x.http2.huffman.HuffmanHelper.*;

public class HuffmanCoding<T> {

    private final HuffmanNode<T> root;
    private final Map<T, HuffmanCodePath> huffmanCode;

    // 根据一个数组来创建树
    public HuffmanCoding(T[] data) {
        // 统计频率
        var map = buildCountMap(data);
        // 构建优先队列
        var queue = buildPriorityQueue(map);
        // 构建霍夫曼树
        this.root = buildHuffmanTree(queue);
        // 初始化霍夫曼编码表
        this.huffmanCode = buildHuffmanCodeTable(this.root);
    }

    // 根据编码表直接创建
    public HuffmanCoding(Map<T, String> huffmanCode) {
        this.huffmanCode = normalHuffmanCode(huffmanCode); // 转换成 HuffmanCodePath
        this.root = buildHuffmanTreeFromCode(this.huffmanCode);
    }

    // 编码方法
    public HuffmanCodePath encode(T symbol) {
        if (!huffmanCode.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol not found in Huffman code table: " + symbol);
        }
        return huffmanCode.get(symbol);
    }

    // 批量编码
    public HuffmanCodePath encode(T[] data) {
        var paths = new ArrayList<HuffmanCodePath>();

        for (T symbol : data) {
            var path = encode(symbol);
            paths.add(path);
        }

        return HuffmanCodePath.concat(paths);
    }

    // 解码方法
    public T decode(HuffmanCodePath path) {
        HuffmanNode<T> current = root;

        for (int i = 0; i < path.length(); i++) {
            if (current == null) {
                throw new IllegalStateException("Invalid Huffman code path: reached a null node.");
            }

            current = path.bitSet().get(i) ? current.right : current.left;
        }

        if (current == null || !current.isLeaf()) {
            throw new IllegalStateException("Invalid Huffman code path: did not reach a leaf node.");
        }

        return current.value;
    }

    // 批量解码
    public T[] decode(HuffmanCodePath path, Class<T> type) {
        var list = new ArrayList<T>();
        HuffmanNode<T> current = root;

        for (int i = 0; i < path.length(); i++) {
            current = path.bitSet().get(i) ? current.right : current.left;

            if (current == null) {
                throw new IllegalStateException("Invalid Huffman code path: reached a null node.");
            }

            if (current.isLeaf()) {
                list.add(current.value);
                current = root; // 重置到根节点
            }
        }

        if (current != root) {
            throw new IllegalStateException("Invalid Huffman code path: leftover bits.");
        }

        // 将结果转换为数组
        @SuppressWarnings("unchecked")
        T[] result = (T[]) java.lang.reflect.Array.newInstance(type, list.size());
        return list.toArray(result);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("HuffmanCoding:\n");
        huffmanCode.forEach((key, value) -> sb.append(key).append(" -> ").append(value).append("\n"));
        return sb.toString();
    }

}
