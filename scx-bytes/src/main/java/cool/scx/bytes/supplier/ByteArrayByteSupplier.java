package cool.scx.bytes.supplier;

import cool.scx.bytes.ByteChunk;
import cool.scx.collections.ArrayIterator;
import cool.scx.collections.SingleIterator;

import java.util.Collection;
import java.util.Iterator;

/// ByteArrayByteSupplier
///
/// @author scx567888
/// @version 0.0.1
public class ByteArrayByteSupplier implements ByteSupplier {

    private final Iterator<byte[]> byteArrayIterator;

    public ByteArrayByteSupplier(Collection<byte[]> byteArrays) {
        this.byteArrayIterator = byteArrays.iterator();
    }

    public ByteArrayByteSupplier(byte[]... byteArrays) {
        this.byteArrayIterator = new ArrayIterator<>(byteArrays);
    }

    public ByteArrayByteSupplier(byte[] byteArray) {
        this.byteArrayIterator = new SingleIterator<>(byteArray);
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
