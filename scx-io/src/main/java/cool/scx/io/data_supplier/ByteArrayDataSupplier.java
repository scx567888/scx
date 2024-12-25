package cool.scx.io.data_supplier;

import cool.scx.io.data_node.DataNode;

import java.util.Iterator;
import java.util.List;

public class ByteArrayDataSupplier implements DataSupplier {

    private final Iterator<byte[]> byteArrayIterator;

    public ByteArrayDataSupplier(List<byte[]> byteArrays) {
        this.byteArrayIterator = byteArrays.iterator();
    }

    public ByteArrayDataSupplier(byte[]... byteArrays) {
        this.byteArrayIterator = List.of(byteArrays).iterator();
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
