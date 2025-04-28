package cool.scx.http.x.http1.fixed_length;

import cool.scx.io.data_consumer.DataNodeDataConsumer;
import cool.scx.io.data_node.DataNode;
import cool.scx.io.data_reader.DataReader;
import cool.scx.io.data_supplier.DataSupplier;
import cool.scx.io.exception.NoMoreDataException;

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
            //这里我们将 原始 dataReader 中的 dataNode 中的 bytes 直接拷贝 实现尽量少的 数据复制
            var consumer = new DataNodeDataConsumer();
            dataReader.read(consumer, remaining, 1);// 我们只尝试拉取一次
            var dataNode = consumer.getDataNode();
            remaining -= dataNode.available();
            return dataNode;
        } catch (NoMoreDataException e) {
            // 如果底层 DataReader 没数据了，也返回 null
            return null;
        }
    }

}
