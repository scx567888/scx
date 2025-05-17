package cool.scx.io.data_reader;

import cool.scx.io.data_consumer.ByteArrayDataConsumer;
import cool.scx.io.data_consumer.DataConsumer;
import cool.scx.io.data_indexer.ByteIndexer;
import cool.scx.io.data_indexer.DataIndexer;
import cool.scx.io.data_indexer.KMPDataIndexer;
import cool.scx.io.exception.DataSupplierException;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.exception.NoMoreDataException;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.io.data_consumer.SkipDataConsumer.SKIP_DATA_CONSUMER;
import static java.lang.Math.toIntExact;

/// DataReader
///
/// @author scx567888
/// @version 0.0.1
public interface DataReader {

    /// 读取单个字节 (指针会移动)
    /// 当没有更多的数据时会抛出异常
    ///
    /// @return byte
    /// @throws NoMoreDataException 没有更多数据时抛出
    byte read() throws NoMoreDataException, DataSupplierException;

    /// 向 dataConsumer 写入指定长度字节 (指针会移动)
    /// 当没有更多的数据时会抛出异常
    ///
    /// @param dataConsumer 消费者
    /// @param maxLength    最大长度
    /// @param maxPullCount 最大长度
    /// @throws NoMoreDataException 没有更多数据时抛出
    void read(DataConsumer dataConsumer, long maxLength, long maxPullCount) throws NoMoreDataException, DataSupplierException;

    /// 查看单个字节 (指针会移动)
    /// 当没有更多的数据时会抛出异常
    ///
    /// @return byte
    /// @throws NoMoreDataException 没有更多数据时抛出
    byte peek() throws NoMoreDataException, DataSupplierException;

    /// 向 dataConsumer 写入指定长度字节 (指针不会移动)
    /// 当没有更多的数据时会抛出异常
    ///
    /// @param dataConsumer 消费者
    /// @param maxLength    最大长度
    /// @param maxPullCount 最大长度
    /// @throws NoMoreDataException 没有更多数据时抛出
    void peek(DataConsumer dataConsumer, long maxLength, long maxPullCount) throws NoMoreDataException, DataSupplierException;

    /// 查找索引
    long indexOf(DataIndexer indexer, long maxLength, long maxPullCount) throws NoMatchFoundException, NoMoreDataException, DataSupplierException;

    /// 标记
    void mark();

    /// 重置
    void reset();

    /// InputStream 写法的 read
    int inputStreamRead() throws IOException;

    /// InputStream 写法的 read
    int inputStreamRead(byte[] b, int off, int len) throws IOException;

    /// InputStream 写法的 TransferTo
    long inputStreamTransferTo(OutputStream out, long maxLength) throws IOException;
    
    /// InputStream 写法的 read
    byte[] inputStreamReadNBytes(long len) throws IOException;

    default byte[] read(int maxLength) throws NoMoreDataException, DataSupplierException {
        return read(maxLength, Long.MAX_VALUE);
    }

    default byte[] read(int maxLength, long maxPullCount) throws NoMoreDataException, DataSupplierException {
        var consumer = new ByteArrayDataConsumer();
        read(consumer, maxLength, maxPullCount);
        return consumer.getBytes();
    }

    default void read(DataConsumer dataConsumer, long maxLength) throws NoMoreDataException, DataSupplierException {
        read(dataConsumer, maxLength, Long.MAX_VALUE);
    }

    default byte[] peek(int maxLength) throws NoMoreDataException, DataSupplierException {
        return peek(maxLength, Long.MAX_VALUE);
    }

    default byte[] peek(int maxLength, long maxPullCount) throws NoMoreDataException, DataSupplierException {
        var consumer = new ByteArrayDataConsumer();
        peek(consumer, maxLength, maxPullCount);
        return consumer.getBytes();
    }

    default void peek(DataConsumer dataConsumer, long maxLength) throws NoMoreDataException, DataSupplierException {
        peek(dataConsumer, maxLength, Long.MAX_VALUE);
    }

    default void skip(long length) throws NoMoreDataException, DataSupplierException {
        skip(length, Long.MAX_VALUE);
    }

    default void skip(long length, long maxPullCount) throws NoMoreDataException, DataSupplierException {
        read(SKIP_DATA_CONSUMER, length, maxPullCount);
    }

    default long indexOf(byte b) throws NoMatchFoundException, NoMoreDataException, DataSupplierException {
        return indexOf(b, Long.MAX_VALUE);
    }

    default long indexOf(byte b, long maxLength) throws NoMatchFoundException, NoMoreDataException, DataSupplierException {
        return indexOf(b, maxLength, Long.MAX_VALUE);
    }

    default long indexOf(byte b, long maxLength, long maxPullCount) throws NoMatchFoundException, NoMoreDataException, DataSupplierException {
        return indexOf(new ByteIndexer(b), maxLength, maxPullCount);
    }

    default long indexOf(byte[] b) throws NoMatchFoundException, NoMoreDataException, DataSupplierException {
        return indexOf(b, Long.MAX_VALUE);
    }

    default long indexOf(byte[] b, long maxLength) throws NoMatchFoundException, NoMoreDataException, DataSupplierException {
        return indexOf(b, maxLength, Long.MAX_VALUE);
    }

    default long indexOf(byte[] b, long maxLength, long maxPullCount) throws NoMatchFoundException, NoMoreDataException, DataSupplierException {
        return indexOf(new KMPDataIndexer(b), maxLength, maxPullCount);
    }

    default byte[] readUntil(byte b) throws NoMatchFoundException, NoMoreDataException, DataSupplierException {
        var index = indexOf(b);
        var data = read(toIntExact(index));
        skip(1);
        return data;
    }

    default byte[] readUntil(byte b, int maxLength) throws NoMatchFoundException, NoMoreDataException, DataSupplierException {
        var index = indexOf(b, maxLength);
        var data = read(toIntExact(index));
        skip(1);
        return data;
    }

    default byte[] readUntil(byte[] b) throws NoMatchFoundException, NoMoreDataException, DataSupplierException {
        var index = indexOf(b);
        var data = read(toIntExact(index));
        skip(b.length);
        return data;
    }

    default byte[] readUntil(byte[] b, int maxLength) throws NoMatchFoundException, NoMoreDataException, DataSupplierException {
        var index = indexOf(b, maxLength);
        var data = read(toIntExact(index));
        skip(b.length);
        return data;
    }

    default byte[] peekUntil(byte b) throws NoMatchFoundException, NoMoreDataException, DataSupplierException {
        var index = indexOf(b);
        return peek(toIntExact(index));
    }

    default byte[] peekUntil(byte b, int maxLength) throws NoMatchFoundException, NoMoreDataException, DataSupplierException {
        var index = indexOf(b, maxLength);
        return peek(toIntExact(index));
    }

    default byte[] peekUntil(byte[] b) throws NoMatchFoundException, NoMoreDataException, DataSupplierException {
        var index = indexOf(b);
        return peek(toIntExact(index));
    }

    default byte[] peekUntil(byte[] b, int maxLength) throws NoMatchFoundException, NoMoreDataException, DataSupplierException {
        var index = indexOf(b, maxLength);
        return peek(toIntExact(index));
    }

    default long inputStreamTransferTo(OutputStream out) throws IOException {
        return inputStreamTransferTo(out, Long.MAX_VALUE);
    }

}
