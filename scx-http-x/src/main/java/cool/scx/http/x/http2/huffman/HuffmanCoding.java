package cool.scx.http.x.http2.huffman;

import static cool.scx.http.x.http2.huffman.HuffmanHelper.*;

public class HuffmanCoding<T> {

    //根据一个数组来创建 树
    public HuffmanCoding(T[] data) {
        // 统计频率
        var map = buildCountMap(data);
        // 构建优先队列
        var queue = buildPriorityQueue(map);
        // 构建霍夫曼树
        var tree = buildHuffmanTree(queue);
        System.out.println(tree);
    }

    public static void main(String[] args) {
        var a = new HuffmanCoding<>(new String[]{"a", "a", "b", "c", "d", "e", "f"});
        System.out.println();
    }

}
