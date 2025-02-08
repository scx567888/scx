package cool.scx.common.bit_array;

import java.util.Arrays;

public class BitArray implements IBitArray {

    // 掩码静态查找表
    private static final byte[] BIT_MASKS = {
            (byte) 0x80, (byte) 0x40, (byte) 0x20, (byte) 0x10,
            (byte) 0x08, (byte) 0x04, (byte) 0x02, (byte) 0x01
    };

    private byte[] data; // 用字节数组存储位
    private int length; // 当前的位数组长度（实际的位数）
    private long capacity; // 当前容量（以位为单位）

    public BitArray() {
        this(new byte[0], 0);
    }

    public BitArray(byte[] data, int length) {
        this.data = data;
        this.capacity = (long) data.length << 3;
        this.length = length;
    }

    @Override
    public void set(int index, boolean value) {
        ensureCapacityAndLength(index);//确保容量并更新长度
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

    //确保容量和长度
    private void ensureCapacityAndLength(int index) {
        if (index >= length) {
            length = index + 1; //更新长度
        }

        if (index >= capacity) {
            int newByteSize = Math.max((index + 8) >> 3, data.length + (data.length >> 1));
            data = Arrays.copyOf(data, newByteSize);
            capacity = (long) newByteSize << 3;
        }
    }

    private int byteIndex(int index) {
        return index >> 3;
    }

    private int bitIndex(int index) {
        return index & 7;
    }

}
