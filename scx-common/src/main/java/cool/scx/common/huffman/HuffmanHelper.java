package cool.scx.common.huffman;

import cool.scx.common.bit_array.BitArray;
import cool.scx.common.bit_array.IBitArray;
import cool.scx.common.count_map.CountMap;
import cool.scx.common.count_map.ICountMap;
import cool.scx.common.util.$;

import java.util.*;

import static java.util.Comparator.comparingInt;

public class HuffmanHelper {

    // 统计频率
    public static <T> CountMap<T> buildCountMap(T[] data) {
        return $.countingBy(data);
    }

    // 构建优先队列
    public static <T> PriorityQueue<HuffmanNode<T>> buildPriorityQueue(ICountMap<T> map) {
        var queue = new PriorityQueue<HuffmanNode<T>>(comparingInt(a -> a.frequency));
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

    public static <T> Map<T, IBitArray> normalHuffmanCode(Map<T, String> huffmanCode) {
        var map = new HashMap<T, IBitArray>();
        for (var e : huffmanCode.entrySet()) {
            map.put(e.getKey(), new BitArray(e.getValue()));
        }
        return map;
    }

    // 构建霍夫曼编码表 (使用 BitSet)
    public static <T> Map<T, IBitArray> buildHuffmanCodeTable(HuffmanNode<T> root) {
        Map<T, IBitArray> huffmanCode = new HashMap<>();
        buildHuffmanCodeTable0(root, new BitArray(), 0, huffmanCode);
        return huffmanCode;
    }

    // 辅助递归方法
    private static <T> void buildHuffmanCodeTable0(HuffmanNode<T> node, IBitArray path, int length, Map<T, IBitArray> huffmanCode) {
        if (node.isLeaf()) {
            huffmanCode.put(node.value, new BitArray(path.toBytes(), length)); // 保存当前路径
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
    public static <T> HuffmanNode<T> buildHuffmanTreeFromCode(Map<T, IBitArray> huffmanCode) {
        var root = new HuffmanNode<T>(null, 0); // 初始化根节点

        for (var entry : huffmanCode.entrySet()) {
            T symbol = entry.getKey();
            var codePath = entry.getValue();

            HuffmanNode<T> current = root;

            for (int i = 0; i < codePath.length(); i++) {
                if (codePath.get(i)) {
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

    public static void buildTreeString(HuffmanNode<?> node, StringBuilder sb, String prefix) {
        if (node != null) {
            if (node.isLeaf()) {
                sb.append(prefix).append("└── ").append(node.value).append("\n");
            } else {
                sb.append(prefix).append("└── *\n");
                String newPrefix = prefix + "    ";
                buildTreeString(node.left, sb, newPrefix);
                buildTreeString(node.right, sb, newPrefix);
            }
        }
    }

}
