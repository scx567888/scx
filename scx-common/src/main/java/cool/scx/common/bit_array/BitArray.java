package cool.scx.common.bit_array;

import java.util.Arrays;

import static cool.scx.common.bit_array.BitArrayHelper.*;

public class BitArray implements IBitArray {

    byte[] data; // 用字节数组存储位
    int capacity; // 当前容量（以位为单位）
    int length; // 当前的位数组长度（实际的位数）

    public BitArray() {
        this(new byte[0], 0);
    }

    public BitArray(int length) {
        this(new byte[byteLength(length)], length);
    }

    public BitArray(byte[] data) {
        this(data, data.length << 3);// data.length * 8
    }

    public BitArray(byte[] data, int length) {
        this.data = data;
        this.capacity = data.length << 3; // data.length * 8
        if (length > this.capacity) {
            throw new IllegalArgumentException("length 不应该大于容量 capacity");
        }
        this.length = length;
    }

    public BitArray(String binaryString) {
        this();
        setByBinaryString(this, binaryString);
    }

    @Override
    public void set(int index, boolean value) {
        ensureCapacity(index);// 确保容量
        updateLength(index);// 更新长度
        int byteIndex = byteIndex(index);
        int bitIndex = bitIndex(index);
        if (value) {
            data[byteIndex] |= BIT_MASKS[bitIndex];
        } else {
            data[byteIndex] &= (byte) ~BIT_MASKS[bitIndex];
        }
    }

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
            sb.append(get(i) ? '1' : '0');
        }
        return sb.toString();
    }

    @Override
    public void append(IBitArray array) {
        if (array instanceof BitArray p) {
            appendFast(p);
        } else {
            IBitArray.super.append(array);
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
            System.arraycopy(p.data, 0, this.data, byteOffset, pByteLength);
        } else {
            // 情况 2：未字节对齐，需要处理跨字节拼接
            int remainingBits = 8 - bitOffset;
            int pByteLength = byteLength(p.length);
            for (int i = 0; i < pByteLength; i++) {
                byte b = p.data[i];

                // 将 p.data[i] 的高位部分拼接到当前字节的空位
                this.data[byteOffset] |= (byte) ((b & 0xFF) >>> bitOffset);

                // 处理低位部分，拼接到下一个字节
                if (byteOffset + 1 < this.data.length) {
                    this.data[byteOffset + 1] |= (byte) (b << remainingBits);
                } else {
                    // 如果超出当前数组长度，进行扩容
                    this.data = Arrays.copyOf(this.data, this.data.length + 1);
                    this.data[byteOffset + 1] = (byte) (b << remainingBits);
                }
                byteOffset++;
            }
        }

        // 更新长度
        this.length = newLength;
    }

    private void ensureCapacity(int index) {
        if (index >= capacity) {
            int newByteSize = Math.max((index + 8) >> 3, data.length + (data.length >> 1));// 1.5倍 扩容
            data = Arrays.copyOf(data, newByteSize);
            capacity = newByteSize << 3;
        }
    }

    private void updateLength(int index) {
        if (index >= length) {
            length = index + 1;
        }
    }

}
