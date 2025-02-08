package cool.scx.common.bit_array;

import java.util.Arrays;

public class BitArray implements IBitArray {

    // 掩码静态查找表
    private static final byte[] BIT_MASKS = {
            (byte) 0x80, (byte) 0x40, (byte) 0x20, (byte) 0x10,
            (byte) 0x08, (byte) 0x04, (byte) 0x02, (byte) 0x01
    };

    byte[] data; // 用字节数组存储位
    int capacity; // 当前容量（以位为单位）
    int length; // 当前的位数组长度（实际的位数）

    public BitArray() {
        this(new byte[]{});
    }

    public BitArray(int length) {
        int byteLength = byteLength(length); // 计算所需字节数
        this.data = new byte[byteLength];
        this.capacity = byteLength << 3;
        this.length = length;
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
        this();//初始化一下
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

    private static int byteIndex(int index) {
        return index >> 3;
    }

    private static int bitIndex(int index) {
        return index & 7;
    }

    private static int byteLength(int bitLength) {
        return (bitLength + 7) >> 3; // 向上取整，计算最小字节数
    }

    private static void setByBinaryString(BitArray bitArray, String binaryString) {
        int bitIndex = 0; // 当前 BitSet 的索引
        // 遍历字符串
        for (var c : binaryString.toCharArray()) {
            switch (c) {
                case '1' -> {
                    bitArray.set(bitIndex, true); // 仅当字符是 '1' 时设置为 true
                    bitIndex++;
                }
                case '0' -> {
                    bitArray.set(bitIndex, false);
                    bitIndex++;
                } // 仅当字符是 '1' 时设置为 true
                default -> {
                    // 其他字符（分隔符）直接跳过        
                }
            }
        }
    }

}
