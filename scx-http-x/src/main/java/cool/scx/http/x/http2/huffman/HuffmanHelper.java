package cool.scx.http.x.http2.huffman;

import cool.scx.common.count_map.CountMap;
import cool.scx.common.count_map.ICountMap;
import cool.scx.common.util.$;

import java.util.Comparator;
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
    
}
