package cool.scx.common.bit_array;

public class CombinedBitArray implements IBitArray {

    private final BitArray[] bitArrays;
    private final int[] startIndices; // 每个 BitArray 的起始索引
    private final int totalLength;

    public CombinedBitArray(BitArray... bitArrays) {
        this.bitArrays = bitArrays;

        // 初始化 startIndices 并计算总长度
        this.startIndices = new int[bitArrays.length];
        int length = 0;
        for (int i = 0; i < bitArrays.length; i++) {
            startIndices[i] = length;
            length += bitArrays[i].length; // 直接访问 BitArray 的 length 字段
        }
        this.totalLength = length;
    }

    @Override
    public void set(int index, boolean value) {
        checkIndexBounds(index); // 检查索引合法性
        int arrayIndex = findBitArrayIndex(index); // 定位对应的 BitArray
        int relativeIndex = index - startIndices[arrayIndex];
        int byteIndex = relativeIndex >> 3; // 计算字节索引
        int bitIndex = relativeIndex & 7; // 计算位索引

        // 直接操作 data 字段进行设置
        if (value) {
            bitArrays[arrayIndex].data[byteIndex] |= (1 << (7 - bitIndex));
        } else {
            bitArrays[arrayIndex].data[byteIndex] &= ~(1 << (7 - bitIndex));
        }

        // 更新长度
        if (relativeIndex >= bitArrays[arrayIndex].length) {
            bitArrays[arrayIndex].length = relativeIndex + 1;
        }
    }

    @Override
    public boolean get(int index) {
        checkIndexBounds(index); // 检查索引合法性
        int arrayIndex = findBitArrayIndex(index); // 定位对应的 BitArray
        int relativeIndex = index - startIndices[arrayIndex];
        int byteIndex = relativeIndex >> 3; // 计算字节索引
        int bitIndex = relativeIndex & 7; // 计算位索引

        // 直接操作 data 字段进行获取
        return (bitArrays[arrayIndex].data[byteIndex] & (1 << (7 - bitIndex))) != 0;
    }

    @Override
    public int length() {
        return totalLength;
    }

    @Override
    public byte[] toBytes() {
        int totalBytes = (totalLength + 7) / 8; // 计算最终需要的字节数
        byte[] combinedBytes = new byte[totalBytes];

        int currentBitOffset = 0; // 记录全局位偏移量（以位为单位）

        for (BitArray bitArray : bitArrays) {
            byte[] bitArrayData = bitArray.data; // 直接访问 BitArray 的 data
            int bitArrayLength = bitArray.length; // 当前 BitArray 的位长度
            int bitArrayBytes = (bitArrayLength + 7) / 8; // 当前 BitArray 的实际字节数

            // 跨字节拼接
            int startByteOffset = currentBitOffset / 8; // 目标数组中的起始字节偏移
            int startBitOffset = currentBitOffset % 8; // 目标数组中的起始位偏移

            if (startBitOffset == 0) {
                // 字节对齐，直接拷贝
                System.arraycopy(bitArrayData, 0, combinedBytes, startByteOffset, bitArrayBytes);
            } else {
                // 跨字节处理
                for (int i = 0; i < bitArrayBytes; i++) {
                    int combinedIndex = startByteOffset + i;

                    // 当前字节的前部分
                    combinedBytes[combinedIndex] |= (byte) ((bitArrayData[i] & 0xFF) >>> startBitOffset);
                    // 当前字节的后部分，拼接到下一个字节
                    if (combinedIndex + 1 < totalBytes) {
                        combinedBytes[combinedIndex + 1] |= (byte) ((bitArrayData[i] & 0xFF) << (8 - startBitOffset));
                    }
                }
            }

            currentBitOffset += bitArrayLength; // 更新全局位偏移量
        }

        return combinedBytes;
    }

    @Override
    public String toBinaryString() {
        var sb = new StringBuilder(totalLength);
        for (var bitArray : bitArrays) {
            sb.append(bitArray.toBinaryString());
        }
        return sb.toString();
    }

    /**
     * 检查索引是否越界。
     *
     * @param index 要检查的索引
     * @throws IndexOutOfBoundsException 如果索引不在合法范围内
     */
    private void checkIndexBounds(int index) {
        if (index < 0 || index >= totalLength) {
            throw new IndexOutOfBoundsException("索引超出范围: " + index);
        }
    }

    /**
     * 根据全局索引快速定位对应的 BitArray。
     *
     * @param index 全局索引
     * @return 对应的 BitArray 索引
     */
    private int findBitArrayIndex(int index) {
        for (int i = 0; i < startIndices.length; i++) {
            if (index < startIndices[i] + bitArrays[i].length) {
                return i;
            }
        }
        throw new IndexOutOfBoundsException("索引超出范围: " + index); // 理论上不应该触发
    }

}
