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

    private byte[] bytes;
    private int length;

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

    public BitArray(BitArray old) {
        this.bytes = Arrays.copyOf(old.bytes, old.bytes.length);
        this.length = old.length;
    }

    public BitArray(BitArray old, int length) {
        if (length > byteCapacity(old.bytes)) {
            throw new IllegalArgumentException("length 不应该大于总容量 capacity");
        }
        this.bytes = Arrays.copyOf(old.bytes, byteLength(length));
        this.length = length;
    }

    @Override
    public void set(int index, boolean value) {
        _checkIndex(index);
        _set0(index, value);
    }

    @Override
    public void set(int fromIndex, int toIndex, boolean value) throws IndexOutOfBoundsException {
        _checkIndex(fromIndex, toIndex);
        for (int i = fromIndex; i < toIndex; i = i + 1) {
            _set0(i, value);
        }
    }

    @Override
    public boolean get(int index) {
        _checkIndex(index);
        return _get0(index);
    }

    @Override
    public IBitArray get(int fromIndex, int toIndex) throws IndexOutOfBoundsException {
        _checkIndex(fromIndex, toIndex);
        var result = new BitArray(toIndex - fromIndex);
        for (int i = fromIndex; i < toIndex; i = i + 1) {
            result.set(i - fromIndex, _get0(i));
        }
        return result;
    }

    @Override
    public void length(int length) {
        _ensureCapacity(length);
        this.length = length;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public byte[] toBytes() {
        int actualByteLength = byteLength(length);// 这里我们返回最小可容纳的字节数量
        return Arrays.copyOf(bytes, actualByteLength);
    }

    @Override
    public String toBinaryString() {
        var sb = new StringBuilder(length);
        for (int i = 0; i < length; i = i + 1) {
            sb.append(_get0(i) ? '1' : '0');
        }
        return sb.toString();
    }

    @Override
    public void append(boolean value) {
        _ensureCapacity(length);
        _append0(value);
    }

    @Override
    public void append(IBitArray other) {
        _ensureCapacity(this.length + other.length());
        for (var b : other) {
            this._append0(b);
        }
    }

    private void _checkIndex(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("索引 " + index + " 超出范围，长度为 " + length);
        }
    }

    private void _checkIndex(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > length || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("索引范围 (" + fromIndex + ", " + toIndex + ") 超出范围，长度为 " + length);
        }
    }

    //无检查 set
    private void _set0(int index, boolean value) {
        int byteIndex = byteIndex(index);
        int bitIndex = bitIndex(index);
        if (value) {
            bytes[byteIndex] |= BIT_MASKS[bitIndex];
        } else {
            bytes[byteIndex] &= (byte) ~BIT_MASKS[bitIndex];
        }
    }

    //无检查 get
    private boolean _get0(int index) {
        int byteIndex = byteIndex(index);
        int bitIndex = bitIndex(index);
        return (bytes[byteIndex] & BIT_MASKS[bitIndex]) != 0;
    }

    //无检查 append
    private void _append0(boolean value) {
        _set0(length, value);
        length = length + 1;
    }

    //确保容量足够容纳索引所指的大小 , 扩容策略为 (所需最小字节数 或 当前的2倍)
    private void _ensureCapacity(int index) {
        if (index >= byteCapacity(bytes)) {
            var newByteSize = Math.max(byteLength(index + 1), bytes.length << 1);
            bytes = Arrays.copyOf(bytes, newByteSize);
        }
    }

}
