package cool.scx.io;

import cool.scx.common.iterator.ArrayIterator;

import java.util.Iterator;
import java.util.List;

/**
 * 可以一次性将多个 DataSupplier 组合成一个
 */
public class SequenceDataSupplier implements DataSupplier {

    private final Iterator<DataSupplier> iterator;
    private DataSupplier currentSupplier;

    public SequenceDataSupplier(List<DataSupplier> dataSupplierList) {
        this.iterator = dataSupplierList.iterator();
        if (this.iterator.hasNext()) {
            this.currentSupplier = this.iterator.next();
        }
    }

    public SequenceDataSupplier(DataSupplier... dataSupplierList) {
        this.iterator = new ArrayIterator<>(dataSupplierList);
        if (this.iterator.hasNext()) {
            this.currentSupplier = this.iterator.next();
        }
    }

    @Override
    public DataNode get() {
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
