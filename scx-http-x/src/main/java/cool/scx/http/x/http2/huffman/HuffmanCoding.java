package cool.scx.http.x.http2.huffman;

import cool.scx.http.x.http2.hpack.HPACKHuffmanTable;

import java.util.Map;

import static cool.scx.http.x.http2.huffman.HuffmanHelper.*;

public class HuffmanCoding<T> {

    private final HuffmanNode<T> root;
    private final Map<T, HuffmanCodePath> huffmanCode;

    //根据一个数组来创建 树
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

    public HuffmanCoding(Map<T, String> huffmanCode) {
        this.huffmanCode = normalHuffmanCode(huffmanCode);
        this.root = buildHuffmanTreeFromCode(this.huffmanCode);
    }

    public static void main(String[] args) {
        var s=new HuffmanCoding<>(HPACKHuffmanTable.HPACK_HUFFMAN_TABLE);
        System.out.println(s);
    }

}
