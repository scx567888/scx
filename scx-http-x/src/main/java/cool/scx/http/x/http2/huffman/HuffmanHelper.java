package cool.scx.http.x.http2.huffman;

import cool.scx.common.count_map.CountMap;
import cool.scx.common.count_map.ICountMap;
import cool.scx.common.util.$;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

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

    public static <T> HashMap<T, String> buildHuffmanCodeTable(HuffmanNode<T> node) {
        var huffmanCode = new HashMap<T, String>();
        buildHuffmanCodeTable0(node, "", huffmanCode);
        return huffmanCode;
    }

    public static <T> void buildHuffmanCodeTable0(HuffmanNode<T> node, String path, Map<T, String> huffmanCode) {
        if (node.isLeaf()) {
            huffmanCode.put(node.value, path);
            return;
        }
        buildHuffmanCodeTable0(node.left, path + "0", huffmanCode);
        buildHuffmanCodeTable0(node.right, path + "1", huffmanCode);
    }

    public static <T> HuffmanNode<T> buildHuffmanTreeFromCode(Map<T, String> huffmanCode) {
        // 初始化根节点
        var root = new HuffmanNode<T>(null, 0);

        // 遍历编码表
        for (var entry : huffmanCode.entrySet()) {
            T symbol = entry.getKey();
            String code = entry.getValue();

            // 从根节点开始构建
            HuffmanNode<T> current = root;

            for (char c : code.toCharArray()) {
                switch (c) {
                    case '0' -> {
                        // 如果当前位是 '0'，进入或创建左子节点
                        if (current.left == null) {
                            current.left = new HuffmanNode<>(null, 0);
                        }
                        current = current.left;
                    }
                    case '1' -> {
                        // 如果当前位是 '1'，进入或创建右子节点
                        if (current.right == null) {
                            current.right = new HuffmanNode<>(null, 0);
                        }
                        current = current.right;
                    }
                    default -> {
                        //忽略其他的 字符 比如为了方便维护添加的 分隔符
                    }
                }
            }

            // 到达编码末尾，设置叶子节点的值
            current.value = symbol;
        }

        return root;
    }

}
