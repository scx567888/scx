package cool.scx.io.data_supplier;

import cool.scx.io.data_node.DataNode;

import java.util.Iterator;
import java.util.List;

public class SequenceDataSupplier implements DataSupplier {

    private final Iterator<DataSupplier> iterator;
    private DataSupplier currentSupplier;

    public SequenceDataSupplier(List<DataSupplier> dataSupplierList) {
        this.iterator = dataSupplierList.iterator();
        if (this.iterator.hasNext()) {
            this.currentSupplier = this.iterator.next();
        }
    }

    @Override
    public DataNode get() {
        while (currentSupplier != null) {
            DataNode dataNode = currentSupplier.get();
            if (dataNode != null) {
                return dataNode;
            } else if (iterator.hasNext()) {
                currentSupplier = iterator.next();
            } else {
                currentSupplier = null;
            }
        }
        return null; // 所有 DataSupplier 都返回 null，表示结束
    }

}
