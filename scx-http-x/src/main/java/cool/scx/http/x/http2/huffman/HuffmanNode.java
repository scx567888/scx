package cool.scx.http.x.http2.huffman;

/**
 * 霍夫曼树节点类，实现 Comparable 接口以便在优先队列中按照频率排序。
 */
public class HuffmanNode<T> implements Comparable<HuffmanNode<T>> {

    public T value;         // 如果为叶子节点，保存对应字节
    public int frequency;      // 该节点的频率或子树中所有字节的总频率
    public HuffmanNode<T> left;   // 左子节点
    public HuffmanNode<T> right;  // 右子节点

    // 构造叶子节点
    public HuffmanNode(T value, int frequency) {
        this.value = value;
        this.frequency = frequency;
    }

    // 构造内部节点
    public HuffmanNode(int frequency, HuffmanNode<T> left, HuffmanNode<T> right) {
        this.value = null;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    /**
     * 判断是否为叶子节点
     *
     * @return 如果左右子节点均为空，则返回 true
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }

    @Override
    public int compareTo(HuffmanNode<T> other) {
        return Integer.compare(this.frequency, other.frequency);
    }

}
