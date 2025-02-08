package cool.scx.http.x.http2.hpack;

public class HPACKUtils {

    /**
     * 获取 HPACK 字节序列的真正有效比特长度（不包括填充位）。
     *
     * @param byteArray HPACK 编码的字节数组
     * @return 有效比特长度（去掉填充位后的长度）
     * @throws IllegalStateException 如果填充位不符合 HPACK 的规定
     */
    public static int getValidBitLength(byte[] byteArray) {
        // 总比特长度
        int totalBits = byteArray.length * 8;

        // 如果字节数组为空，直接返回 0
        if (byteArray.length == 0) {
            return 0;
        }

        // 最后一个字节
        byte lastByte = byteArray[byteArray.length - 1];

        // 计算填充位长度（最后一个字节的连续 1 数量，从低位开始）
        int paddingBits = countTrailingOnes(lastByte);

        // 如果填充位的数量超过 7 或者没有填充位（paddingBits == 0），返回总比特长度
        if (paddingBits > 7) {
            throw new IllegalStateException("Invalid HPACK Huffman encoding: padding bits are too many.");
        }

        // 验证填充位是否合法（即最后几个位必须是连续的 1）
        if (!isValidPadding(lastByte, paddingBits)) {
            throw new IllegalStateException("Invalid HPACK Huffman encoding: leftover bits are not valid padding.");
        }

        // 有效比特长度等于总比特长度减去填充位长度
        return totalBits - paddingBits;
    }

    /**
     * 计算一个字节中从最低位开始连续的 1 的数量。
     *
     * @param b 要计算的字节
     * @return 从最低位开始连续 1 的数量
     */
    private static int countTrailingOnes(byte b) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            if ((b & (1 << i)) != 0) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    /**
     * 验证填充位是否合法（填充位应该是从最低位连续的 1）。
     *
     * @param b            要验证的字节
     * @param paddingBits 填充位数量
     * @return 填充位是否合法
     */
    private static boolean isValidPadding(byte b, int paddingBits) {
        // 如果没有填充位，则无需验证
        if (paddingBits == 0) {
            return true;
        }

        // 创建一个掩码，用于检查最后几个填充位是否全为 1
        int mask = (1 << paddingBits) - 1;
        return (b & mask) == mask;
    }

}
