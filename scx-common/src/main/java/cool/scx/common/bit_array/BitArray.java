package cool.scx.common.bit_array;

import java.util.Arrays;

import static cool.scx.common.bit_array.BitArrayHelper.*;

/**
 * 基于 byte 数组的实现 优点是相比较 {@link java.util.BitSet} 在数组方面会快一点
 *
 * @author scx567888
 * @version 0.0.1
 */
public class BitArray implements IBitArray {

    private byte[] data; // 用字节数组存储位
    private int length; // 当前的位数组长度（实际的位数）

    public BitArray() {
        this(new byte[0], 0);
    }

    public BitArray(int length) {
        this(new byte[byteLength(length)], length);
    }

    public BitArray(byte[] data) {
        this(data, byteCapacity(data));// data.length * 8
    }

    public BitArray(byte[] data, int length) {
        if (length > byteCapacity(data)) {
            throw new IllegalArgumentException("length 不应该大于总容量 capacity");
        }
        this.data = data;
        this.length = length;
    }

    public BitArray(String binaryString) {
        this();
        setByBinaryString(this, binaryString);
    }

    private void set0(int index, boolean value) {
        int byteIndex = byteIndex(index);
        int bitIndex = bitIndex(index);
        if (value) {
            data[byteIndex] |= BIT_MASKS[bitIndex];
        } else {
            data[byteIndex] &= (byte) ~BIT_MASKS[bitIndex];
        }
    }

    private boolean get0(int index) {
        int byteIndex = byteIndex(index);
        int bitIndex = bitIndex(index);
        return (data[byteIndex] & BIT_MASKS[bitIndex]) != 0;
    }

    @Override
    public void set(int index, boolean value) {
        checkIndex(index);
        set0(index, value);
    }

    @Override
    public boolean get(int index) {
        checkIndex(index);
        return get0(index);
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public void append(boolean value) {
        ensureCapacity(length);// 确保容量
        set0(length, value);
        length = length + 1;// 更新长度
    }

    @Override
    public void append(IBitArray other) {
        if (other instanceof BitArray p) {
            appendFast(p);
        } else {
            IBitArray.super.append(other);
        }
    }

    @Override
    public byte[] toBytes() {
        // 计算实际需要的字节数
        int actualByteLength = byteLength(length); // 向上取整
        // 创建一个新数组，仅复制有效数据部分
        return Arrays.copyOf(data, actualByteLength);
    }

    @Override
    public String toBinaryString() {
        var sb = new StringBuilder(length);
        for (int i = 0; i < length; i = i + 1) {
            sb.append(get0(i) ? '1' : '0');
        }
        return sb.toString();
    }

    private void appendFast(BitArray p) {
        int newLength = this.length + p.length; // 拼接后的总长度
        ensureCapacity(newLength); // 确保容量足够

        int bitOffset = this.length % 8; // 当前字节内的位偏移量
        int byteOffset = this.length / 8; // 当前字节的索引

        if (bitOffset == 0) {
            // 情况 1：字节对齐，直接复制字节数据
            int pByteLength = byteLength(p.length);
            System.arraycopy(p.data, 0, this.data, byteOffset, pByteLength);
        } else {
            // 情况 2：未字节对齐，需要处理跨字节拼接
            int remainingBits = 8 - bitOffset;
            int pByteLength = byteLength(p.length);

            for (int i = 0; i < pByteLength; i++) {
                int bUnsigned = p.data[i] & 0xFF; // 将 byte 转换为无符号 int

                // 将 b 的高位部分拼接到当前字节的空位
                this.data[byteOffset] |= (byte) (bUnsigned >>> bitOffset);

                // 将 b 的低位部分拼接到下一个字节
                this.data[byteOffset + 1] |= (byte) ((bUnsigned << remainingBits) & 0xFF);

                byteOffset++;
            }
        }

        // 更新长度
        this.length = newLength;
    }

    private void ensureCapacity(int index) {
        if (index >= byteCapacity(data)) {
            var newByteSize = Math.max(byteLength(index + 1), data.length << 1);  // 所需的最小字节数 和 2倍 扩容 最大值
            var newData = new byte[newByteSize];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("索引 " + index + " 超出范围，长度为 " + length);
        }
    }

}
