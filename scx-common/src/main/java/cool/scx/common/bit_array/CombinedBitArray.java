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
        checkIndexBounds(index); // 检查索引合法性 因为这种组合视图的模式下 不允许扩容
        var arrayIndex = findBitArrayIndex(index); // 定位对应的 BitArray
        var relativeIndex = index - startIndices[arrayIndex]; // 转换为局部索引
        bitArrays[arrayIndex].set(relativeIndex, value); // 直接调用 BitArray 的 set 方法
    }

    @Override
    public boolean get(int index) {
        checkIndexBounds(index); // 检查索引合法性
        var arrayIndex = findBitArrayIndex(index); // 定位对应的 BitArray
        var relativeIndex = index - startIndices[arrayIndex]; // 转换为局部索引
        return bitArrays[arrayIndex].get(relativeIndex); // 直接调用 BitArray 的 get 方法
    }

    @Override
    public int length() {
        return totalLength;
    }

    @Override
    public byte[] toBytes() {
        int totalBytes = (totalLength + 7) / 8; // 计算最终需要的字节数
        byte[] combinedBytes = new byte[totalBytes];

        int currentBitOffset = 0; // 当前全局位偏移量（以位为单位）

        for (BitArray bitArray : bitArrays) {
            byte[] bitArrayData = bitArray.data; // 直接访问 BitArray 的 data
            int bitArrayLength = bitArray.length; // 当前 BitArray 的位长度
            int bitArrayBytes = (bitArrayLength + 7) >> 3; // 当前 BitArray 的实际字节数

            int startByteOffset = currentBitOffset >> 3; // 目标数组中的起始字节偏移
            int startBitOffset = currentBitOffset % 8; // 目标数组中的起始位偏移

            if (startBitOffset == 0) {
                // 如果对齐字节边界，直接拷贝整个数组
                System.arraycopy(bitArrayData, 0, combinedBytes, startByteOffset, bitArrayBytes);
            } else {
                // 如果不对齐字节边界，需要进行跨字节拼接
                for (int i = 0; i < bitArrayBytes; i++) {
                    int targetIndex = startByteOffset + i;

                    // 将当前字节的高位部分拼接到目标数组
                    combinedBytes[targetIndex] |= (byte) ((bitArrayData[i] & 0xFF) >>> startBitOffset);

                    // 如果下一个字节还在数组范围内，将低位部分拼接到下一字节
                    if (targetIndex + 1 < totalBytes) {
                        combinedBytes[targetIndex + 1] |= (byte) ((bitArrayData[i] & 0xFF) << (8 - startBitOffset));
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
