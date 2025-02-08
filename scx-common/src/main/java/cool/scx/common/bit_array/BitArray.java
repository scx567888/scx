package cool.scx.common.bit_array;

import java.util.Arrays;

public class BitArray implements IBitArray {

    // 静态查找表：下标 0 对应最高位 0x80，依次到下标 7 为最低位 0x01
    private static final byte[] BIT_MASKS = {
            (byte) 0x80, (byte) 0x40, (byte) 0x20, (byte) 0x10,
            (byte) 0x08, (byte) 0x04, (byte) 0x02, (byte) 0x01
    };

    private byte[] data;    // 用字节数组存储位
    private int length;     // 当前的位数组长度（实际的位数）
    private long capacity;  // 当前容量（以位为单位）

    /**
     * 无参构造函数，创建默认的空位数组（初始容量为 0 位，会在第一次 set 时扩容）。
     */
    public BitArray() {
        this(new byte[0], 0);
    }

    /**
     * 带参数的构造函数，使用指定的数据和长度初始化。
     *
     * @param data   初始数据（外部保证不修改）
     * @param length 位数组的长度（位数）
     */
    public BitArray(byte[] data, int length) {
        this.data = data;
        this.capacity = (long) data.length << 3;
        this.length = length;
    }

    @Override
    public void set(int index, boolean value) {
        // 合并 ensureCapacity 和 updateLength 的逻辑
        if (index >= length) {
            length = index + 1;    // 更新长度
            ensureCapacity(); // 确保容量足够
        }

        int byteIndex = byteIndex(index);   // index / 8
        int bitIndex = bitIndex(index);     // index % 8

        // 设置位
        if (value) {
            data[byteIndex] |= BIT_MASKS[bitIndex];  // 设置对应位为 1
        } else {
            data[byteIndex] &= (byte) ~BIT_MASKS[bitIndex]; // 清除对应位为 0
        }
    }

    //已经达到理论上的性能极限 无需优化
    @Override
    public boolean get(int index) {
        int byteIndex = byteIndex(index);
        int bitIndex = bitIndex(index);
        return (data[byteIndex] & BIT_MASKS[bitIndex]) != 0;
    }

    @Override
    public int length() {
        return length;
    }

    /**
     * 直接返回内部数组（注意：调用者需要保证不修改该数组）
     */
    @Override
    public byte[] toBytes() {
        return data;
    }

    @Override
    public String toBinaryString() {
        var sb = new StringBuilder(length);
        for (int i = 0; i < length; i = i + 1) {
            sb.append(get(i) ? '1' : '0');
        }
        return sb.toString();
    }

    //计算 对应的 byte 索引
    private int byteIndex(int index) {
        return index >> 3;
    }

    //计算 对应的 bit 索引
    private int bitIndex(int index) {
        return index & 7;
    }

    private void updateLength(int index) {
        if (index >= length) {
            length = index + 1; // 更新长度
        }
    }

    private void ensureCapacity() {
        if (length <= capacity) {
            return; // 容量足够，直接返回
        }
        // 扩容策略：每次容量翻倍，确保最小需求
        long newCapacity = Math.max(length, capacity * 2);
        int newByteSize = (int) ((newCapacity + 7) >> 3);
        data = Arrays.copyOf(data, newByteSize);
        capacity = (long) newByteSize << 3;
    }

}
