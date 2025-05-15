package cool.scx.io.data_indexer;

import cool.scx.common.util.ArrayUtils;

/// 单字节查找器
///
/// @author scx567888
/// @version 0.0.1
public class ByteIndexer implements DataIndexer {

    private final byte b;

    public ByteIndexer(byte b) {
        this.b = b;
    }

    @Override
    public int indexOf(byte[] bytes, int position, int length) {
        int i = ArrayUtils.indexOf(bytes, position, length, b);
        return i == -1 ? Integer.MIN_VALUE : i;
    }

}
