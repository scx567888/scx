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

    private byte[] bytes; // 用字节数组存储位
    private int length; // 当前的位数组长度（实际的位数）

    public BitArray() {
        this.bytes = new byte[0];
        this.length = 0;
    }

    public BitArray(int length) {
        this.bytes = new byte[byteLength(length)];
        this.length = length;
    }

    public BitArray(byte[] bytes) {
        this.bytes = bytes;
        this.length = byteCapacity(this.bytes);
    }

    public BitArray(byte[] bytes, int length) {
        if (length > byteCapacity(bytes)) {
            throw new IllegalArgumentException("length 不应该大于总容量 capacity");
        }
        this.bytes = bytes;
        this.length = length;
    }

    public BitArray(String binaryString) {
        this();
        setByBinaryString(this, binaryString);
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
        int actualByteLength = byteLength(length);// 计算实际需要的字节数
        return Arrays.copyOf(bytes, actualByteLength);
    }

    @Override
    public String toBinaryString() {
        var sb = new StringBuilder(length);
        for (int i = 0; i < length; i = i + 1) {
            sb.append(get0(i) ? '1' : '0');
        }
        return sb.toString();
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("索引 " + index + " 超出范围，长度为 " + length);
        }
    }

    private void set0(int index, boolean value) {
        int byteIndex = byteIndex(index);
        int bitIndex = bitIndex(index);
        if (value) {
            bytes[byteIndex] |= BIT_MASKS[bitIndex];
        } else {
            bytes[byteIndex] &= (byte) ~BIT_MASKS[bitIndex];
        }
    }

    private boolean get0(int index) {
        int byteIndex = byteIndex(index);
        int bitIndex = bitIndex(index);
        return (bytes[byteIndex] & BIT_MASKS[bitIndex]) != 0;
    }

    private void ensureCapacity(int index) {
        if (index >= byteCapacity(bytes)) {
            var newByteSize = Math.max(byteLength(index + 1), bytes.length << 1);// 所需最小字节数 或 2倍扩容
            bytes = Arrays.copyOf(bytes, newByteSize);
        }
    }

    private void appendFast(BitArray p) {
        int newLength = this.length + p.length; // 拼接后的总长度
        ensureCapacity(newLength); // 确保容量足够

        int bitOffset = this.length % 8; // 当前字节内的位偏移量
        int byteOffset = this.length / 8; // 当前字节的索引

        if (bitOffset == 0) {
            // 情况 1：字节对齐，直接复制字节数据
            int pByteLength = byteLength(p.length);
            System.arraycopy(p.bytes, 0, this.bytes, byteOffset, pByteLength);
        } else {
            // 情况 2：未字节对齐，需要处理跨字节拼接
            int remainingBits = 8 - bitOffset;
            int pByteLength = byteLength(p.length);

            for (int i = 0; i < pByteLength; i++) {
                int bUnsigned = p.bytes[i] & 0xFF; // 将 byte 转换为无符号 int

                // 将 b 的高位部分拼接到当前字节的空位
                this.bytes[byteOffset] |= (byte) (bUnsigned >>> bitOffset);

                // 将 b 的低位部分拼接到下一个字节
                this.bytes[byteOffset + 1] |= (byte) ((bUnsigned << remainingBits) & 0xFF);

                byteOffset++;
            }
        }

        // 更新长度
        this.length = newLength;
    }

}
