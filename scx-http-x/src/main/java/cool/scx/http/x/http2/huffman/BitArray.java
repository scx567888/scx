package cool.scx.http.x.http2.huffman;

import cool.scx.common.bit_array.IBitArray;
import java.util.Arrays;

public class BitArray implements IBitArray {

    private byte[] data; // 用字节数组存储位
    private long length; // 当前的位数组长度（实际的位数）
    private long capacity; // 当前容量（以位为单位）

    /**
     * 无参构造函数，创建默认的空位数组（初始容量为 64 位）。
     */
    public BitArray() {
        this.data = new byte[8]; // 初始容量为 8 字节（64 位）
        this.capacity = 64; // 初始容量为 64 位
        this.length = 0; // 初始长度为 0
    }

    /**
     * 带参数的构造函数，使用指定的数据和长度初始化。
     *
     * @param data   初始数据
     * @param length 位数组的长度（位数）
     */
    public BitArray(byte[] data, long length) {
        if (data == null || length < 0 || length > data.length * 8) {
            throw new IllegalArgumentException("Invalid data or length");
        }
        this.data = Arrays.copyOf(data, data.length); // 深拷贝
        this.capacity = data.length * 8;
        this.length = length;
    }

    @Override
    public void set(long index, boolean value) {
        ensureCapacity(index + 1); // 确保容量足够
        if (index >= length) {
            length = index + 1; // 更新长度
        }
        int byteIndex = (int) (index / 8); // 对应的字节索引
        int bitIndex = (int) (index % 8);  // 对应字节中的位索引
        if (value) {
            data[byteIndex] |= (1 << (7 - bitIndex)); // 将对应位置1
        } else {
            data[byteIndex] &= ~(1 << (7 - bitIndex)); // 将对应位清0
        }
    }

    @Override
    public boolean get(long index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        int byteIndex = (int) (index / 8); // 对应的字节索引
        int bitIndex = (int) (index % 8);  // 对应字节中的位索引
        return (data[byteIndex] & (1 << (7 - bitIndex))) != 0;
    }

    @Override
    public long length() {
        return length;
    }

    @Override
    public boolean[] toBooleans() {
        boolean[] booleans = new boolean[(int) length];
        for (int i = 0; i < length; i++) {
            booleans[i] = get(i);
        }
        return booleans;
    }

    @Override
    public byte[] toBytes() {
        int byteCount = (int) Math.ceil((double) length / 8);
        return Arrays.copyOf(data, byteCount);
    }

    @Override
    public long[] toLongs() {
        int longCount = (int) Math.ceil((double) length / 64);
        long[] longs = new long[longCount];
        for (int i = 0; i < length; i++) {
            if (get(i)) {
                int longIndex = (int) (i / 64);
                int bitIndex = (int) (i % 64);
                longs[longIndex] |= (1L << (63 - bitIndex));
            }
        }
        return longs;
    }

    @Override
    public String toBinaryString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(get(i) ? '1' : '0');
        }
        return sb.toString();
    }

    /**
     * 确保容量足够存储指定数量的位。
     *
     * @param minCapacity 需要的最小容量（以位为单位）
     */
    private void ensureCapacity(long minCapacity) {
        if (minCapacity <= capacity) {
            return; // 当前容量足够
        }
        long newCapacity = Math.max(minCapacity, capacity * 2); // 扩容到两倍容量或至少满足需要
        int newByteSize = (int) Math.ceil((double) newCapacity / 8);
        data = Arrays.copyOf(data, newByteSize);
        capacity = newByteSize * 8;
    }
}
