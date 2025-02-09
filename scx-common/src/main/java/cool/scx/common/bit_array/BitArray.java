package cool.scx.common.bit_array;

import java.util.Arrays;

/**
 * 基于 byte 数组的实现 优点是相比较 {@link java.util.BitSet} 在数组方面会快一点
 *
 * @author scx567888
 * @version 0.0.1
 */
public class BitArray implements IBitArray {

    // 掩码静态查找表
    public static final byte[] BIT_MASKS = {
            (byte) 0x80, (byte) 0x40, (byte) 0x20, (byte) 0x10,
            (byte) 0x08, (byte) 0x04, (byte) 0x02, (byte) 0x01
    };

    private byte[] data; // 用字节数组存储位
    private int capacity; // 当前容量（以位为单位）
    private int length; // 当前的位数组长度（实际的位数）

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

    public static int byteIndex(int index) {
        return index >> 3;
    }

    public static int bitIndex(int index) {
        return index & 7;
    }

    public static int byteLength(int bitLength) {
        return (bitLength + 7) >> 3; // 向上取整，计算最小字节数
    }

    public static void setByBinaryString(BitArray bitArray, String binaryString) {
        // 遍历字符串
        for (var c : binaryString.toCharArray()) {
            // 其他字符（分隔符）直接跳过        
            if (c == '1') {
                bitArray.append(true); // 仅当字符是 '1' 时设置为 true
            } else if (c == '0') {
                bitArray.append(false); // 仅当字符是 '1' 时设置为 true
            }
        }
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
        if (index >= capacity) {
            int newByteSize = Math.max((index + 8) >> 3, data.length + (data.length >> 1));// 1.5倍 扩容
            data = Arrays.copyOf(data, newByteSize);
            capacity = newByteSize << 3;
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("索引 " + index + " 超出范围，长度为 " + length);
        }
    }

}
