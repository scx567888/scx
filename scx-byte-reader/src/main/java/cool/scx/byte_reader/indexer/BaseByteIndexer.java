package cool.scx.byte_reader.indexer;

import cool.scx.byte_reader.ByteChunk;
import cool.scx.common.util.ArrayUtils;

/// 单字节查找器
///
/// @author scx567888
/// @version 0.0.1
public class BaseByteIndexer implements ByteIndexer {

    private final byte b;

    public BaseByteIndexer(byte b) {
        this.b = b;
    }

    @Override
    public int indexOf(ByteChunk chunk) {
        int i = ArrayUtils.indexOf(chunk.bytes, chunk.startPosition, chunk.length, b);
        return i == -1 ? Integer.MIN_VALUE : i;
    }

}
