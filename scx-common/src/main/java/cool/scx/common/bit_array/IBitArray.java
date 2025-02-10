package cool.scx.common.bit_array;

import java.util.Iterator;

/**
 * BitArray 可以理解为一个 boolean[] (bit 数组)
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface IBitArray extends Iterable<Boolean> {

    /**
     * 设置某一个位 (等同于数组的 xxx[index] = value , 不会自动扩容)
     *
     * @param index 索引
     * @param value bit
     * @throws IndexOutOfBoundsException 索引越界异常
     */
    void set(int index, boolean value) throws IndexOutOfBoundsException;

    /**
     * 获取某一个位 (等同于数组的 value = xxx[index] , 不会自动扩容)
     *
     * @param index 索引
     * @return bit
     * @throws IndexOutOfBoundsException 索引越界异常
     */
    boolean get(int index) throws IndexOutOfBoundsException;

    /**
     * 设置长度
     */
    void length(int length);

    /**
     * 获取长度
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
     * 追加另一个 bitArray (会自动扩容)
     *
     * @param other bitArray
     */
    default void append(IBitArray other) {
        for (var b : other) {
            append(b);
        }
    }

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
