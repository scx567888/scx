package cool.scx.common.bit_array;

import java.util.Arrays;

public class BitArray implements IBitArray {

    // 掩码静态查找表
    private static final byte[] BIT_MASKS = {
            (byte) 0x80, (byte) 0x40, (byte) 0x20, (byte) 0x10,
            (byte) 0x08, (byte) 0x04, (byte) 0x02, (byte) 0x01
    };

    private byte[] data; // 用字节数组存储位
    private long capacity; // 当前容量（以位为单位）
    private int length; // 当前的位数组长度（实际的位数）

    public BitArray() {
        this(new byte[0], 0);
    }

    public BitArray(byte[] data, int length) {
        this.data = data;
        this.capacity = (long) data.length << 3;
        this.length = length;
    }

    public BitArray(String binaryString) {
        this();//初始化一下
        int bitIndex = 0; // 当前 BitSet 的索引
        // 遍历字符串
        for (var c : binaryString.toCharArray()) {
            switch (c) {
                case '1' -> {
                    this.set(bitIndex, true); // 仅当字符是 '1' 时设置为 true
                    bitIndex++;
                }
                case '0' -> {
                    this.set(bitIndex, false);
                    bitIndex++;
                } // 仅当字符是 '1' 时设置为 true
                default -> {
                    // 其他字符（分隔符）直接跳过        
                }
            }
        }
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
        return data;
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
            int newByteSize = Math.max((index + 8) >> 3, data.length + (data.length >> 1));
            data = Arrays.copyOf(data, newByteSize);
            capacity = (long) newByteSize << 3;
        }
    }

    private void updateLength(int index) {
        if (index >= length) {
            length = index + 1;
        }
    }

    private int byteIndex(int index) {
        return index >> 3;
    }

    private int bitIndex(int index) {
        return index & 7;
    }

}
