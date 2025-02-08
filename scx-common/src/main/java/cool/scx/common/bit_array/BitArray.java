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
        ensureCapacity(newLength + 7); // 提前扩容，预留额外空间

        int currentByteOffset = this.length / 8; // 当前最后一个字节的索引
        int currentBitOffset = this.length % 8; // 当前最后一个字节的位偏移量

        if (currentBitOffset == 0) {
            // 情况 1: 当前位数组按字节对齐，直接拷贝字节
            System.arraycopy(p.data, 0, this.data, currentByteOffset, byteLength(p.length));
        } else {
            // 情况 2: 跨字节拼接
            int otherByteLength = byteLength(p.length);
            for (int i = 0; i < otherByteLength; i++) {
                byte otherByte = p.data[i];

                // 将 otherByte 的高位移入当前字节的低位空闲部分
                this.data[currentByteOffset] |= (byte) ((otherByte & 0xFF) >>> currentBitOffset);

                // 如果有跨字节操作，将 otherByte 的低位写入下一字节
                this.data[currentByteOffset + 1] |= (byte) (otherByte << (8 - currentBitOffset));

                currentByteOffset++; // 移动到下一个目标字节
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
