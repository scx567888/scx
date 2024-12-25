package cool.scx.io;

import cool.scx.common.util.ArrayUtils;

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
