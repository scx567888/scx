package cool.scx.bytes.indexer;

import cool.scx.bytes.ByteChunk;

/// 单字节查找器
///
/// @author scx567888
/// @version 0.0.1
public class SimpleByteIndexer implements ByteIndexer {

    private final byte b;

    public SimpleByteIndexer(byte b) {
        this.b = b;
    }

    @Override
    public int indexOf(ByteChunk chunk) {
        var bytes = chunk.bytes;
        var start = chunk.startPosition;
        var end = chunk.endPosition;
        //普通 查找
        for (var i = start; i < end; i = i + 1) {
            if (bytes[i] == b) {
                return i - start;
            }
        }
        return Integer.MIN_VALUE;
    }

}
