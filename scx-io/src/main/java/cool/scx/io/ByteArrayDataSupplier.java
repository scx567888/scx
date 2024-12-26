package cool.scx.io;

import cool.scx.common.iterator.ArrayIterator;
import cool.scx.common.iterator.SingleIterator;

import java.util.Collection;
import java.util.Iterator;

public class ByteArrayDataSupplier implements DataSupplier {

    private final Iterator<byte[]> byteArrayIterator;

    public ByteArrayDataSupplier(Collection<byte[]> byteArrays) {
        this.byteArrayIterator = byteArrays.iterator();
    }

    public ByteArrayDataSupplier(byte[]... byteArrays) {
        this.byteArrayIterator = new ArrayIterator<>(byteArrays);
    }

    public ByteArrayDataSupplier(byte[] byteArray) {
        this.byteArrayIterator = new SingleIterator<>(byteArray);
    }

    @Override
    public DataNode get() {
        if (byteArrayIterator.hasNext()) {
            byte[] nextArray = byteArrayIterator.next();
            return new DataNode(nextArray);
        }
        return null; // 没有更多字节数组时返回 null
    }

}
