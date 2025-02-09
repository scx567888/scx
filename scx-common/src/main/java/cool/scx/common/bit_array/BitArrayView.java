package cool.scx.common.bit_array;

/**
 * 一个只读的 BitArray 视图
 *
 * @author scx567888
 * @version 0.0.1
 */
public class BitArrayView implements IBitArray {

    private final IBitArray[] bitArrays;
    private final int[] startIndices; // 每个 BitArray 的起始索引
    private final int totalLength;

    public BitArrayView(IBitArray... bitArrays) {
        this.bitArrays = bitArrays;

        // 初始化 startIndices 并计算总长度
        this.startIndices = new int[bitArrays.length];
        int length = 0;
        for (int i = 0; i < bitArrays.length; i++) {
            startIndices[i] = length;
            length += bitArrays[i].length(); // 直接访问 BitArray 的 length 字段
        }
        this.totalLength = length;
    }

    @Override
    public void set(int index, boolean value) {
        throw new UnsupportedOperationException("视图不支持 set !!!");
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
    public void append(boolean value) {
        throw new UnsupportedOperationException("视图不支持 append !!!");
    }

    @Override
    public byte[] toBytes() {
        var ba = new BitArray(totalLength);
        for (var bitArray : bitArrays) {
            ba.append(bitArray);
        }
        return ba.toBytes();
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
            throw new IndexOutOfBoundsException("索引超出范围: " + index + ", 总长度: " + totalLength);
        }
    }

    /**
     * 根据全局索引快速定位对应的 BitArray。
     *
     * @param index 全局索引
     * @return 对应的 BitArray 索引
     */
    private int findBitArrayIndex(int index) {
        int low = 0, high = startIndices.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            if (index < startIndices[mid]) {
                high = mid - 1;
            } else if (mid == startIndices.length - 1 || index < startIndices[mid + 1]) {
                return mid;
            } else {
                low = mid + 1;
            }
        }
        throw new IndexOutOfBoundsException("索引超出范围: " + index); // 理论上不应该触发
    }

}
