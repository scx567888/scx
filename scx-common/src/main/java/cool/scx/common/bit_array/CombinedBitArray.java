package cool.scx.common.bit_array;

public class CombinedBitArray implements IBitArray {

    private final BitArray[] bitArrays;
    private final int totalLength;

    public CombinedBitArray(BitArray... bitArrays) {
        this.bitArrays = bitArrays;
        // 计算总长度
        int length = 0;
        for (var bitArray : bitArrays) {
            length += bitArray.length();
        }
        this.totalLength = length;
    }

    @Override
    public void set(int index, boolean value) {
        // 查找属于哪个 BitArray
        for (var bitArray : bitArrays) {
            if (index < bitArray.length()) {
                bitArray.set(index, value);
                return;
            }
            index -= bitArray.length(); // 更新索引
        }
    }

    @Override
    public boolean get(int index) {
        // 查找属于哪个 BitArray
        for (var bitArray : bitArrays) {
            if (index < bitArray.length()) {
                return bitArray.get(index);
            }
            index -= bitArray.length(); // 更新索引
        }
        return false;
    }

    @Override
    public int length() {
        return totalLength;
    }

    @Override
    public byte[] toBytes() {
        // 计算总的字节数
        int totalBytes = (totalLength + 7) / 8; // 向上取整
        byte[] combinedBytes = new byte[totalBytes];

        int currentBitOffset = 0; // 当前总偏移量（以 bit 为单位）
        for (BitArray bitArray : bitArrays) {
            byte[] bitArrayBytes = bitArray.toBytes();
            int bitArrayLength = bitArray.length(); // 当前 BitArray 的 bit 长度

            int startByteOffset = currentBitOffset / 8; // 当前写入的起始字节
            int startBitOffset = currentBitOffset % 8; // 当前写入的起始位偏移

            if (startBitOffset == 0) {
                // 如果刚好对齐字节，直接拷贝字节数据
                int numBytesToCopy = (bitArrayLength + 7) / 8;
                System.arraycopy(bitArrayBytes, 0, combinedBytes, startByteOffset, numBytesToCopy);
            } else {
                // 如果没有对齐字节，需要逐字节处理跨字节的位拼接
                int byteIndex = 0;
                for (int i = 0; i < bitArrayLength; i++) {
                    if (i % 8 == 0 && i > 0) {
                        byteIndex++;
                    }

                    boolean bitValue = (bitArrayBytes[byteIndex] & (1 << (7 - (i % 8)))) != 0;
                    int globalBitIndex = currentBitOffset + i;
                    int globalByteIndex = globalBitIndex / 8;
                    int globalBitOffset = globalBitIndex % 8;

                    if (bitValue) {
                        combinedBytes[globalByteIndex] |= (byte) (1 << (7 - globalBitOffset));
                    }
                }
            }

            currentBitOffset += bitArrayLength; // 更新偏移量
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
    
}
