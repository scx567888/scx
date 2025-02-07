package cool.scx.http.x.http2.huffman;

import cool.scx.common.count_map.CountMap;
import cool.scx.common.count_map.ICountMap;
import cool.scx.common.util.$;

import java.util.*;

public class HuffmanHelper {

    // 统计频率
    public static <T> CountMap<T> buildCountMap(T[] data) {
        return $.countingBy(data);
    }

    // 构建优先队列
    public static <T> PriorityQueue<HuffmanNode<T>> buildPriorityQueue(ICountMap<T> map) {
        var queue = new PriorityQueue<HuffmanNode<T>>(Comparator.comparingInt(a -> a.frequency));
        for (var entry : map) {
            queue.offer(new HuffmanNode<>(entry.getKey(), entry.getValue().intValue()));
        }
        return queue;
    }

    public static <T> HuffmanNode<T> buildHuffmanTree(PriorityQueue<HuffmanNode<T>> queue) {
        while (queue.size() > 1) {
            // 取出频率最小的两个节点
            var left = queue.poll();
            var right = queue.poll();

            // 创建新的内部节点（没有字符，频率是左右节点频率之和）
            var parent = new HuffmanNode<>(left.frequency + right.frequency, left, right);

            // 将新节点加入优先队列
            queue.add(parent);
        }
        // 队列中最后一个节点就是霍夫曼树的根节点
        return queue.poll();
    }

    public static <T> Map<T, HuffmanCodePath> normalHuffmanCode(Map<T, String> huffmanCode) {
        var map = new HashMap<T, HuffmanCodePath>();
        for (var e : huffmanCode.entrySet()) {
            map.put(e.getKey(), HuffmanCodePath.fromBinaryString(e.getValue()));
        }
        return map;
    }

    // 构建霍夫曼编码表 (使用 BitSet)
    public static <T> Map<T, HuffmanCodePath> buildHuffmanCodeTable(HuffmanNode<T> root) {
        Map<T, HuffmanCodePath> huffmanCode = new HashMap<>();
        buildHuffmanCodeTable0(root, new BitSet(), 0, huffmanCode);
        return huffmanCode;
    }

    // 辅助递归方法
    private static <T> void buildHuffmanCodeTable0(HuffmanNode<T> node, BitSet path, int length, Map<T, HuffmanCodePath> huffmanCode) {
        if (node.isLeaf()) {
            huffmanCode.put(node.value, new HuffmanCodePath((BitSet) path.clone(), length)); // 保存当前路径
            return;
        }
        if (node.left != null) {
            path.set(length, false); // 左子节点设置为 0
            buildHuffmanCodeTable0(node.left, path, length + 1, huffmanCode);
        }
        if (node.right != null) {
            path.set(length, true); // 右子节点设置为 1
            buildHuffmanCodeTable0(node.right, path, length + 1, huffmanCode);
        }
    }

    // 从编码表还原霍夫曼树
    public static <T> HuffmanNode<T> buildHuffmanTreeFromCode(Map<T, HuffmanCodePath> huffmanCode) {
        var root = new HuffmanNode<T>(null, 0); // 初始化根节点

        for (var entry : huffmanCode.entrySet()) {
            T symbol = entry.getKey();
            HuffmanCodePath codePath = entry.getValue();

            HuffmanNode<T> current = root;

            for (int i = 0; i < codePath.length(); i++) {
                if (codePath.bitSet().get(i)) {
                    // 如果当前位是 '1'，进入或创建右子节点
                    if (current.right == null) {
                        current.right = new HuffmanNode<>(null, 0);
                    }
                    current = current.right;
                } else {
                    // 如果当前位是 '0'，进入或创建左子节点
                    if (current.left == null) {
                        current.left = new HuffmanNode<>(null, 0);
                    }
                    current = current.left;
                }
            }
            // 到达路径末尾，设置叶子节点的值
            current.value = symbol;
        }

        return root;
    }

}
