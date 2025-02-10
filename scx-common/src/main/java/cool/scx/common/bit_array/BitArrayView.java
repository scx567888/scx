package cool.scx.common.bit_array;

/**
 * 一个 BitArray 视图
 *
 * @author scx567888
 * @version 0.0.1
 */
public class BitArrayView implements IBitArray {

    private final IBitArray[] bitArrays;
    private final int[] startIndices; // 每个 BitArray 的起始索引
    private final int length;

    public BitArrayView(IBitArray... bitArrays) {
        this.bitArrays = bitArrays;

        // 初始化 startIndices 并计算总长度
        this.startIndices = new int[bitArrays.length];
        int length = 0;
        for (int i = 0; i < bitArrays.length; i++) {
            startIndices[i] = length;
            length += bitArrays[i].length(); // 直接访问 BitArray 的 length 字段
        }
        this.length = length;
    }

    @Override
    public void set(int index, boolean value) throws IndexOutOfBoundsException {
        _checkIndex(index); // 检查索引合法性
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
        _checkIndex(index); // 检查索引合法性
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
        throw new UnsupportedOperationException("视图不支持设置 length !!!");
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public byte[] toBytes() {
        var ba = new BitArray(length);
        for (var bitArray : bitArrays) {
            ba.append(bitArray);
        }
        return ba.toBytes();
    }

    @Override
    public String toBinaryString() {
        var sb = new StringBuilder(length);
        for (var bitArray : bitArrays) {
            sb.append(bitArray.toBinaryString());
        }
        return sb.toString();
    }

    @Override
    public void append(boolean value) {
        throw new UnsupportedOperationException("视图不支持 append !!!");
    }

    @Override
    public void append(IBitArray other) {
        throw new UnsupportedOperationException("视图不支持 append !!!");
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

    private void _set0(int index, boolean value) {
        var arrayIndex = _findBitArrayIndex(index); // 定位对应的 BitArray
        var relativeIndex = index - startIndices[arrayIndex]; // 转换为局部索引
        bitArrays[arrayIndex].set(relativeIndex, value); // 直接调用 BitArray 的 set 方法
    }

    private boolean _get0(int index) {
        var arrayIndex = _findBitArrayIndex(index); // 定位对应的 BitArray
        var relativeIndex = index - startIndices[arrayIndex]; // 转换为局部索引
        return bitArrays[arrayIndex].get(relativeIndex); // 直接调用 BitArray 的 get 方法
    }

    // 根据全局索引快速定位对应的 BitArray。
    private int _findBitArrayIndex(int index) {
        int low = 0;
        int high = startIndices.length - 1;

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
