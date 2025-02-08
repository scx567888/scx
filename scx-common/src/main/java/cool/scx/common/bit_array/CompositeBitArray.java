package cool.scx.common.bit_array;

import java.util.ArrayList;
import java.util.List;

public class CompositeBitArray implements IBitArray {

    private final List<BitArray> bitArrays; // 存储多个 BitArray
    private final long totalLength;        // 组合后的总长度

    /**
     * 构造函数，传递多个 BitArray。
     *
     * @param arrays 要组合的 BitArray
     */
    public CompositeBitArray(BitArray... arrays) {
        if (arrays == null || arrays.length == 0) {
            throw new IllegalArgumentException("At least one BitArray is required.");
        }
        this.bitArrays = new ArrayList<>();
        long length = 0;
        for (BitArray array : arrays) {
            if (array == null) {
                throw new IllegalArgumentException("BitArray cannot be null.");
            }
            this.bitArrays.add(array);
            length += array.length(); // 计算总长度
        }
        this.totalLength = length;
    }

    @Override
    public void set(long index, boolean value) {
        if (index < 0 || index >= totalLength) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        // 找到对应的 BitArray
        for (BitArray array : bitArrays) {
            if (index < array.length()) {
                array.set(index, value);
                return;
            }
            index -= array.length(); // 调整索引到下一个 BitArray
        }
    }

    @Override
    public boolean get(long index) {
        if (index < 0 || index >= totalLength) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        // 找到对应的 BitArray
        for (BitArray array : bitArrays) {
            if (index < array.length()) {
                return array.get(index);
            }
            index -= array.length(); // 调整索引到下一个 BitArray
        }
        return false; // 不可能到这里
    }

    @Override
    public long length() {
        return totalLength;
    }

    @Override
    public boolean[] toBooleans() {
        boolean[] result = new boolean[(int) totalLength];
        int offset = 0;
        for (BitArray array : bitArrays) {
            boolean[] partial = array.toBooleans();
            System.arraycopy(partial, 0, result, offset, partial.length);
            offset += partial.length;
        }
        return result;
    }

    @Override
    public byte[] toBytes() {
        // 计算总字节数
        int totalBytes = (int) Math.ceil((double) totalLength / 8);
        byte[] result = new byte[totalBytes];
        int byteOffset = 0;
        int bitOffset = 0;

        for (BitArray array : bitArrays) {
            byte[] partial = array.toBytes();
            for (byte b : partial) {
                if (bitOffset == 0) {
                    result[byteOffset] = b;
                } else {
                    // 处理字节跨界情况
                    result[byteOffset] |= (b & 0xFF) >>> bitOffset;
                    result[byteOffset + 1] = (byte) (b << (8 - bitOffset));
                }
                byteOffset++;
            }
        }
        return result;
    }

    @Override
    public long[] toLongs() {
        // 计算总长整型数
        int totalLongs = (int) Math.ceil((double) totalLength / 64);
        long[] result = new long[totalLongs];
        int longOffset = 0;
        int bitOffset = 0;

        for (BitArray array : bitArrays) {
            long[] partial = array.toLongs();
            for (long l : partial) {
                if (bitOffset == 0) {
                    result[longOffset] = l;
                } else {
                    // 处理长整型跨界情况
                    result[longOffset] |= l >>> bitOffset;
                    if (longOffset + 1 < totalLongs) {
                        result[longOffset + 1] = l << (64 - bitOffset);
                    }
                }
                longOffset++;
            }
        }
        return result;
    }

    @Override
    public String toBinaryStrings() {
        StringBuilder sb = new StringBuilder();
        for (BitArray array : bitArrays) {
            sb.append(array.toBinaryStrings());
        }
        return sb.toString();
    }

}
