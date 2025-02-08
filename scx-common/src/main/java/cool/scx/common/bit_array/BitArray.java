package cool.scx.common.bit_array;

import java.util.Arrays;

public class BitArray implements IBitArray {

    private byte[] data;    // 用字节数组存储位
    private int length;    // 当前的位数组长度（实际的位数）
    private long capacity;  // 当前容量（以位为单位）

    /**
     * 无参构造函数，创建默认的空位数组（初始容量为 64 位）。
     */
    public BitArray() {
        this(new byte[0], 0);
    }

    /**
     * 带参数的构造函数，使用指定的数据和长度初始化。
     *
     * @param data   初始数据
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
        int byteIndex = index / 8; // 对应的字节索引
        int bitIndex = index % 8;  // 对应字节中的位索引
        if (value) {
            data[byteIndex] |= (1 << (7 - bitIndex)); // 将对应位设置为1
        } else {
            data[byteIndex] &= ~(1 << (7 - bitIndex)); // 将对应位清0
        }
    }

    @Override
    public boolean get(int index) {
        int byteIndex = index / 8;
        int bitIndex = index % 8;
        return (data[byteIndex] & (1 << (7 - bitIndex))) != 0;
    }

    @Override
    public int length() {
        return length;
    }

    /**
     * 将位数组转换为 byte 数组。
     * 对于最后一个字节中未使用的低位，进行清零处理。
     */
    @Override
    public byte[] toBytes() {
        return data;
    }

    /**
     * 将位数组转换为二进制字符串表示，位按照顺序排列（'1' 或 '0'）。
     */
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
     *
     * @param minCapacity 需要的最小容量（以位为单位）
     */
    private void ensureCapacity(long minCapacity) {
        if (minCapacity <= capacity) {
            return; // 当前容量足够
        }
        long newCapacity = Math.max(minCapacity, capacity * 2);
        int newByteSize = (int) Math.ceil((double) newCapacity / 8);
        // 检查扩容后的字节数是否溢出
        if (newByteSize < 0) {
            throw new OutOfMemoryError("Required array size too large");
        }
        data = Arrays.copyOf(data, newByteSize);
        capacity = newByteSize * 8L;
    }

}
