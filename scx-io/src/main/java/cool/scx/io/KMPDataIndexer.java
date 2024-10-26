package cool.scx.io;

public class KMPDataIndexer implements DataIndexer {

    private final int[] lps;
    private final byte[] pattern;
    private int patternIndex;

    public KMPDataIndexer(byte[] pattern) {
        this.pattern = pattern;
        this.patternIndex = 0; // 模式串索引
        this.lps = computeLPSArray(pattern);// 创建部分匹配表
    }

    public static int[] computeLPSArray(byte[] pattern) {
        int[] lps = new int[pattern.length];
        int length = 0;
        int i = 1;

        while (i < pattern.length) {
            if (pattern[i] == pattern[length]) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    public int indexOf(byte[] bytes, int position, int length) {
        //KMP 查找
        for (int i = position; i < position + length; i++) {
            while (patternIndex > 0 && bytes[i] != pattern[patternIndex]) {
                patternIndex = lps[patternIndex - 1];
            }

            if (bytes[i] == pattern[patternIndex]) {
                patternIndex++;
            }

            if (patternIndex == pattern.length) {
                // i - n.position 的意义在于我们不需要包含当前 节点的偏移量 所以减去
                return i - position - patternIndex + 1;
            }
        }
        return -1;
    }

}
