package cool.scx.bytes.indexer;

import cool.scx.bytes.ByteChunk;

/// KMPDataIndexer
///
/// @author scx567888
/// @version 0.0.1
public class KMPByteIndexer implements ByteIndexer {

    private final int[] lps;
    private final byte[] pattern;
    private int patternIndex;

    public KMPByteIndexer(byte[] pattern) {
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

    @Override
    public int indexOf(ByteChunk chunk) {
        var bytes = chunk.bytes;
        var start = chunk.startPosition;
        var end = chunk.endPosition;
        //KMP 查找
        for (int i = start; i < end; i = i + 1) {
            while (patternIndex > 0 && bytes[i] != pattern[patternIndex]) {
                patternIndex = lps[patternIndex - 1];
            }

            if (bytes[i] == pattern[patternIndex]) {
                patternIndex = patternIndex + 1;
            }

            if (patternIndex == pattern.length) {
                // i - n.position 的意义在于我们不需要包含当前 节点的偏移量 所以减去
                var result = i - start - patternIndex + 1;
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

    public int patternIndex() {
        return patternIndex;
    }

    public byte[] pattern() {
        return pattern;
    }

}
