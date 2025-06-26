package cool.scx.bytes.indexer;

import cool.scx.bytes.ByteChunk;

/// SimpleByteIndexer
///
/// @author scx567888
/// @version 0.0.1
public final class SingleByteIndexer implements ByteIndexer {

    private final byte b;

    public SingleByteIndexer(byte b) {
        this.b = b;
    }

    @Override
    public int indexOf(ByteChunk chunk) {
        //普通 查找
        for (var i = 0; i < chunk.length; i = i + 1) {
            if (chunk.getByte(i) == b) {
                return i;
            }
        }
        return Integer.MIN_VALUE;
    }

}
