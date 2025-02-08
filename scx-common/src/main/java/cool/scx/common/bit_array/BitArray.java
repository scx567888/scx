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
        if (array instanceof BitArray otherBitArray) {
            // 高效拼接两个 BitArray 的 data
            int newLength = this.length + otherBitArray.length;

            // 确保容量足够存储拼接后的数据
            ensureCapacity(newLength - 1);

            int currentBitOffset = this.length % 8; // 当前的位偏移
            int currentByteOffset = this.length / 8; // 当前的字节偏移

            if (currentBitOffset == 0) {
                // 对齐字节边界，直接复制字节
                System.arraycopy(otherBitArray.data, 0, this.data, currentByteOffset, byteLength(otherBitArray.length));
            } else {
                // 跨字节拼接
                for (int i = 0; i < otherBitArray.length; i++) {
                    int bitValue = otherBitArray.get(i) ? 1 : 0;
                    set(this.length + i, bitValue == 1);
                }
            }

            // 更新长度
            this.length = newLength;
        } else {
            IBitArray.super.append(array);
        }
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
