package cool.scx.common.huffman;

import cool.scx.common.bit_array.BitArrayView;
import cool.scx.common.bit_array.IBitArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cool.scx.common.huffman.HuffmanHelper.*;

public class HuffmanCodec<T> {

    private final HuffmanNode<T> root;// 跟节点
    private final Map<T, IBitArray> huffmanCode; //编码表

    // 根据一个数组来创建树
    public HuffmanCodec(T[] data) {
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
    public HuffmanCodec(Map<T, String> huffmanCode) {
        this.huffmanCode = normalHuffmanCode(huffmanCode); // 转换成 HuffmanCodePath
        this.root = buildHuffmanTreeFromCode(this.huffmanCode);
    }

    // 编码方法
    public IBitArray encode(T symbol) {
        var result = huffmanCode.get(symbol);
        if (result == null) {
            throw new IllegalArgumentException("Symbol " + symbol + " not found");
        }
        return result;
    }

    // 批量编码
    public IBitArray encode(List<T> data) {
        var paths = new IBitArray[data.size()];
        var i = 0;
        for (T symbol : data) {
            paths[i] = encode(symbol);
            i = i + 1;
        }
        return new BitArrayView(paths);
    }

    // 批量编码
    public IBitArray encode(T[] data) {
        var paths = new IBitArray[data.length];
        var i = 0;
        for (T symbol : data) {
            paths[i] = encode(symbol);
            i = i + 1;
        }
        return new BitArrayView(paths);
    }

    // 解码方法
    public T decodeSingle(IBitArray path) {
        var current = root;

        //从路径遍历 
        for (int i = 0; i < path.length(); i++) {
            if (current == null) {
                throw new IllegalStateException("Invalid Huffman code path: reached a null node.");
            }
            current = path.get(i) ? current.right : current.left;
        }

        //全部变量完成如果没有 找到任何 或者不是叶子节点 则抛出异常
        if (current == null || !current.isLeaf()) {
            throw new IllegalStateException("Invalid Huffman code path: did not reach a leaf node.");
        }

        return current.value;
    }

    // 批量解码
    public List<T> decode(IBitArray path) {
        var list = new ArrayList<T>();
        var current = root;

        for (int i = 0; i < path.length(); i++) {
            if (current == null) {
                throw new IllegalStateException("Invalid Huffman code path: reached a null node.");
            }
            current = path.get(i) ? current.right : current.left;
            
            //如果是叶子节点
            if (current.isLeaf()) {
                list.add(current.value);
                current = root; // 重置到根节点
            }
        }

        if (current != root) {
            throw new IllegalStateException("Invalid Huffman code path: leftover bits.");
        }

        return list;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder("HuffmanCodec:\n");

        // 显示编码表
        sb.append("编码表:\n");
        huffmanCode.forEach((key, value) -> sb.append(key).append(" -> ").append(value.toBinaryString()).append("\n"));

        // 显示霍夫曼树
        sb.append("霍夫曼树:\n");
        buildTreeString(root, sb, "");

        return sb.toString();
    }

}
