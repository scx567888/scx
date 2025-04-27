package cool.scx.http.x.http1.chunked;

import cool.scx.io.data_node.DataNode;
import cool.scx.io.data_reader.DataReader;
import cool.scx.io.data_supplier.DataSupplier;
import cool.scx.io.exception.NoMoreDataException;

import static java.lang.Math.min;

public class FixedLengthDataSupplier implements DataSupplier {

    private final DataReader dataReader;
    private long remaining;

    public FixedLengthDataSupplier(DataReader dataReader, long maxLength) {
        this.dataReader = dataReader;
        this.remaining = maxLength;
    }

    @Override
    public DataNode get() {
        if (remaining <= 0) {
            return null;
        }
        try {
            // 每次尽量多读一点，但不能超过 remaining
            int readLength = (int) min(8192, remaining); // 8 KB 一块，可以调整
            var bytes = dataReader.read(readLength, 1);//我们只尝试拉取一次 
            remaining -= bytes.length;
            return new DataNode(bytes);
        } catch (NoMoreDataException e) {
            // 如果底层 DataReader 没数据了，也返回 null
            return null;
        }
    }

}
