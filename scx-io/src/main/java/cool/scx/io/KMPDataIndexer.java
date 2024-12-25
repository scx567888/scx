package cool.scx.io;

/**
 * KMPDataIndexer
 *
 * @author scx567888
 * @version 0.0.1
 */
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
                length = length + 1;
                lps[i] = length;
                i = i + 1;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i = i + 1;
                }
            }
        }

        return lps;
    }

    public int indexOf(byte[] bytes, int position, int length) {
        var end = position + length;
        //KMP 查找
        for (int i = position; i < end; i = i + 1) {
            while (patternIndex > 0 && bytes[i] != pattern[patternIndex]) {
                patternIndex = lps[patternIndex - 1];
            }

            if (bytes[i] == pattern[patternIndex]) {
                patternIndex = patternIndex + 1;
            }

            if (patternIndex == pattern.length) {
                // i - n.position 的意义在于我们不需要包含当前 节点的偏移量 所以减去
                var result = i - position - patternIndex + 1;
                // 重置 patternIndex 为 0, 保证下次匹配 
                patternIndex = 0;
                return result;
            }
        }
        return Integer.MIN_VALUE;
    }

    public void reset() {
        patternIndex = 0;
    }

    public byte[] pattern() {
        return pattern;
    }

}
