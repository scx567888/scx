package cool.scx.bytes.supplier;

import cool.scx.bytes.ByteChunk;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/// ByteArrayByteSupplier
///
/// @author scx567888
/// @version 0.0.1
public final class ByteArrayByteSupplier implements ByteSupplier {

    private final Iterator<byte[]> byteArrayIterator;

    public ByteArrayByteSupplier(Collection<byte[]> byteArrays) {
        this.byteArrayIterator = byteArrays.iterator();
    }

    public ByteArrayByteSupplier(byte[]... byteArrays) {
        this.byteArrayIterator = List.of(byteArrays).iterator();
    }

    public ByteArrayByteSupplier(byte[] byteArray) {
        this.byteArrayIterator = List.of(byteArray).iterator();
    }

    @Override
    public ByteChunk get() {
        if (byteArrayIterator.hasNext()) {
            byte[] nextArray = byteArrayIterator.next();
            return new ByteChunk(nextArray);
        }
        return null; // 没有更多字节数组时返回 null
    }

}
