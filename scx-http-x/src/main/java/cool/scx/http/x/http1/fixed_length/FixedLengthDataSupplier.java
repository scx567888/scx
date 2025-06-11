package cool.scx.http.x.http1.fixed_length;

import cool.scx.byte_reader.ByteNode;
import cool.scx.byte_reader.ByteReader;
import cool.scx.byte_reader.consumer.ByteNodeByteConsumer;
import cool.scx.byte_reader.exception.NoMoreDataException;
import cool.scx.byte_reader.supplier.ByteSupplier;

public class FixedLengthDataSupplier implements ByteSupplier {

    private final ByteReader dataReader;
    private long remaining;

    public FixedLengthDataSupplier(ByteReader dataReader, long maxLength) {
        this.dataReader = dataReader;
        this.remaining = maxLength;
    }

    @Override
    public ByteNode get() {
        if (remaining <= 0) {
            return null;
        }
        try {
            // 这里我们直接引用 原始 dataReader 中的 bytes，避免了数组的多次拷贝
            var consumer = new ByteNodeByteConsumer();
            dataReader.read(consumer, remaining, 1);// 我们只尝试拉取一次
            var dataNode = consumer.getDataNode();
            remaining -= dataNode.available();
            return dataNode;
        } catch (NoMoreDataException e) {
            // 如果底层 ByteReader 没数据了，也返回 null
            return null;
        }
    }

}
