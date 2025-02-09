package cool.scx.common.bit_array;

import java.util.Iterator;

/**
 * BitArray 可以理解为一个 boolean[]
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface IBitArray extends Iterable<Boolean> {

    /**
     * 设置某一个位 (不会自动扩容)
     *
     * @param index 索引
     * @param value bit
     * @throws IndexOutOfBoundsException 索引越界异常
     */
    void set(int index, boolean value) throws IndexOutOfBoundsException;

    /**
     * 获取某一个位 (不会自动扩容)
     *
     * @param index 索引
     * @return bit
     * @throws IndexOutOfBoundsException 索引越界异常
     */
    boolean get(int index) throws IndexOutOfBoundsException;

    /**
     * 长度
     *
     * @return 长度
     */
    int length();

    /**
     * 追加单个 bit (会自动扩容)
     *
     * @param value v
     */
    void append(boolean value);

    /**
     * 追加另一个 bitArray
     *
     * @param bitArray bitArray
     */
    void append(IBitArray bitArray);

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

    @Override
    default Iterator<Boolean> iterator() {
        return new BitArrayIterator(this);
    }

}
