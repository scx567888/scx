package cool.scx.common.bit_array;

import java.util.Arrays;

public class BitArray implements IBitArray {

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
        this.capacity = data.length * 8L;
        this.length = length;
    }

    @Override
    public void set(int index, boolean value) {
        ensureCapacity(index + 1); // 确保容量足够
        if (index >= length) {
            length = index + 1; // 更新长度
        }
        // 用位运算替换除法和取模
        int byteIndex = index >> 3;  // index / 8
        int bitIndex = index & 7;    // index % 8
        int shift = 7 - bitIndex;    // 大端序：高位在前
        if (value) {
            data[byteIndex] |= (byte) (1 << shift);
        } else {
            data[byteIndex] &= (byte) ~(1 << shift);
        }
    }

    @Override
    public boolean get(int index) {
        int byteIndex = index >> 3;
        int bitIndex = index & 7;
        int shift = 7 - bitIndex;    // 大端序：高位在前
        return (data[byteIndex] & 1 << shift) != 0;
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
        for (int i = 0; i < length; i++) {
            sb.append(get(i) ? '1' : '0');
        }
        return sb.toString();
    }

    /**
     * 确保容量足够存储至少 minCapacity 个位。
     * 采用整数运算替换 Math.ceil，避免浮点数转换。
     *
     * @param minCapacity 需要的最小容量（以位为单位）
     */
    private void ensureCapacity(long minCapacity) {
        if (minCapacity <= capacity) {
            return;
        }
        long newCapacity = Math.max(minCapacity, capacity * 2);
        // 计算新字节数组大小：(newCapacity + 7) / 8 等价于向上取整 newCapacity/8
        int newByteSize = (int) ((newCapacity + 7) / 8);
        data = Arrays.copyOf(data, newByteSize);
        capacity = newByteSize * 8L;
    }

}
