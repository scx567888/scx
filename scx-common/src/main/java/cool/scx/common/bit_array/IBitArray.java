package cool.scx.common.bit_array;

/**
 * BitArray 基本上等同于 {@link java.util.BitSet}
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface IBitArray {

    /**
     * 设置某一个位
     *
     * @param index 索引
     * @param value bit
     */
    void set(int index, boolean value);

    /**
     * 获取某一个位
     *
     * @param index 索引
     * @return bit
     */
    boolean get(int index);

    /**
     * 长度
     *
     * @return l
     */
    int length();

    /**
     * 转换为字节 (注意因为字节无法完全表示 BitArray 在序列化时请结合 length 使用)
     *
     * @return a
     */
    byte[] toBytes();

    /**
     * 转换为 二进制字符串 (0101001001 这种形式)
     *
     * @return s
     */
    String toBinaryString();

    /**
     * 追加
     *
     * @param array a
     */
    default void append(IBitArray array) {
        var nowLength = this.length();
        for (int i = 0; i < array.length(); i++) {
            set(nowLength + i, array.get(i));
        }
    }

}
