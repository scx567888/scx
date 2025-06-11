package cool.scx.byte_reader.supplier;

import cool.scx.byte_reader.ByteNode;
import cool.scx.common.iterator.ArrayIterator;
import cool.scx.common.iterator.SingleIterator;

import java.util.Collection;
import java.util.Iterator;

/// ByteArrayDataSupplier
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
    public ByteNode get() {
        if (byteArrayIterator.hasNext()) {
            byte[] nextArray = byteArrayIterator.next();
            return new ByteNode(nextArray);
        }
        return null; // 没有更多字节数组时返回 null
    }

}
