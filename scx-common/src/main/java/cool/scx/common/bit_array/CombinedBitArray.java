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
            byte[] bitArrayData = bitArray.data; // 直接访问 BitArray 的 data
            int bitArrayLength = bitArray.length(); // 当前 BitArray 的 bit 长度
            int bitArrayBytes = (bitArrayLength + 7) / 8; // 当前 BitArray 的实际字节数

            int startByteOffset = currentBitOffset / 8; // 写入的起始字节
            int startBitOffset = currentBitOffset % 8; // 写入的起始位偏移

            if (startBitOffset == 0) {
                // 对齐字节边界，直接拷贝
                System.arraycopy(bitArrayData, 0, combinedBytes, startByteOffset, bitArrayBytes);
            } else {
                // 跨字节拼接
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
