package cool.scx.byte_reader.supplier;

import cool.scx.common.iterator.ArrayIterator;
import cool.scx.byte_reader.ByteNode;
import cool.scx.byte_reader.exception.ByteSupplierException;

import java.util.Iterator;
import java.util.List;

/// 可以一次性将多个 DataSupplier 组合成一个
///
/// @author scx567888
/// @version 0.0.1
public class SequenceByteSupplier implements ByteSupplier {

    private final Iterator<ByteSupplier> iterator;
    private ByteSupplier currentSupplier;

    public SequenceByteSupplier(List<ByteSupplier> dataSupplierList) {
        this.iterator = dataSupplierList.iterator();
        if (this.iterator.hasNext()) {
            this.currentSupplier = this.iterator.next();
        }
    }

    public SequenceByteSupplier(ByteSupplier... dataSupplierList) {
        this.iterator = new ArrayIterator<>(dataSupplierList);
        if (this.iterator.hasNext()) {
            this.currentSupplier = this.iterator.next();
        }
    }

    @Override
    public ByteNode get() throws ByteSupplierException {
        while (currentSupplier != null) {
            var dataNode = currentSupplier.get();
            if (dataNode != null) {
                return dataNode;
            }
            if (iterator.hasNext()) {
                currentSupplier = iterator.next();
            } else {
                currentSupplier = null;
            }
        }
        return null; // 所有 DataSupplier 都返回 null，表示结束
    }

}
