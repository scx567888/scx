package cool.scx.byte_reader;

import cool.scx.byte_reader.consumer.ByteArrayDataConsumer;
import cool.scx.byte_reader.consumer.ByteConsumer;
import cool.scx.byte_reader.indexer.BaseByteIndexer;
import cool.scx.byte_reader.indexer.ByteIndexer;
import cool.scx.byte_reader.indexer.KMPByteIndexer;
import cool.scx.byte_reader.exception.ByteSupplierException;
import cool.scx.byte_reader.exception.NoMatchFoundException;
import cool.scx.byte_reader.exception.NoMoreDataException;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.byte_reader.consumer.SkipDataConsumer.SKIP_DATA_CONSUMER;
import static java.lang.Math.toIntExact;

/// DataReader
///
/// @author scx567888
/// @version 0.0.1
public interface IByteReader {

    /// 读取单个字节 (指针会移动)
    /// 当没有更多的数据时会抛出异常
    ///
    /// @return byte
    /// @throws NoMoreDataException 没有更多数据时抛出
    byte read() throws NoMoreDataException, ByteSupplierException;

    /// 向 dataConsumer 写入指定长度字节 (指针会移动)
    /// 当没有更多的数据时会抛出异常
    ///
    /// @param dataConsumer 消费者
    /// @param maxLength    最大长度
    /// @param maxPullCount 最大长度
    /// @throws NoMoreDataException 没有更多数据时抛出
    void read(ByteConsumer dataConsumer, long maxLength, long maxPullCount) throws NoMoreDataException, ByteSupplierException;

    /// 查看单个字节 (指针会移动)
    /// 当没有更多的数据时会抛出异常
    ///
    /// @return byte
    /// @throws NoMoreDataException 没有更多数据时抛出
    byte peek() throws NoMoreDataException, ByteSupplierException;

    /// 向 dataConsumer 写入指定长度字节 (指针不会移动)
    /// 当没有更多的数据时会抛出异常
    ///
    /// @param dataConsumer 消费者
    /// @param maxLength    最大长度
    /// @param maxPullCount 最大长度
    /// @throws NoMoreDataException 没有更多数据时抛出
    void peek(ByteConsumer dataConsumer, long maxLength, long maxPullCount) throws NoMoreDataException, ByteSupplierException;

    /// 查找索引
    long indexOf(ByteIndexer indexer, long maxLength, long maxPullCount) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException;

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

    default byte[] read(int maxLength) throws NoMoreDataException, ByteSupplierException {
        return read(maxLength, Long.MAX_VALUE);
    }

    default byte[] read(int maxLength, long maxPullCount) throws NoMoreDataException, ByteSupplierException {
        var consumer = new ByteArrayDataConsumer();
        read(consumer, maxLength, maxPullCount);
        return consumer.getBytes();
    }

    default void read(ByteConsumer dataConsumer, long maxLength) throws NoMoreDataException, ByteSupplierException {
        read(dataConsumer, maxLength, Long.MAX_VALUE);
    }

    default byte[] peek(int maxLength) throws NoMoreDataException, ByteSupplierException {
        return peek(maxLength, Long.MAX_VALUE);
    }

    default byte[] peek(int maxLength, long maxPullCount) throws NoMoreDataException, ByteSupplierException {
        var consumer = new ByteArrayDataConsumer();
        peek(consumer, maxLength, maxPullCount);
        return consumer.getBytes();
    }

    default void peek(ByteConsumer dataConsumer, long maxLength) throws NoMoreDataException, ByteSupplierException {
        peek(dataConsumer, maxLength, Long.MAX_VALUE);
    }

    default void skip(long length) throws NoMoreDataException, ByteSupplierException {
        skip(length, Long.MAX_VALUE);
    }

    default void skip(long length, long maxPullCount) throws NoMoreDataException, ByteSupplierException {
        read(SKIP_DATA_CONSUMER, length, maxPullCount);
    }

    default long indexOf(byte b) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        return indexOf(b, Long.MAX_VALUE);
    }

    default long indexOf(byte b, long maxLength) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        return indexOf(b, maxLength, Long.MAX_VALUE);
    }

    default long indexOf(byte b, long maxLength, long maxPullCount) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        return indexOf(new BaseByteIndexer(b), maxLength, maxPullCount);
    }

    default long indexOf(byte[] b) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        return indexOf(b, Long.MAX_VALUE);
    }

    default long indexOf(byte[] b, long maxLength) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        return indexOf(b, maxLength, Long.MAX_VALUE);
    }

    default long indexOf(byte[] b, long maxLength, long maxPullCount) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        return indexOf(new KMPByteIndexer(b), maxLength, maxPullCount);
    }

    default byte[] readUntil(byte b) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        var index = indexOf(b);
        var data = read(toIntExact(index));
        skip(1);
        return data;
    }

    default byte[] readUntil(byte b, int maxLength) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        var index = indexOf(b, maxLength);
        var data = read(toIntExact(index));
        skip(1);
        return data;
    }

    default byte[] readUntil(byte[] b) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        var index = indexOf(b);
        var data = read(toIntExact(index));
        skip(b.length);
        return data;
    }

    default byte[] readUntil(byte[] b, int maxLength) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        var index = indexOf(b, maxLength);
        var data = read(toIntExact(index));
        skip(b.length);
        return data;
    }

    default byte[] peekUntil(byte b) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        var index = indexOf(b);
        return peek(toIntExact(index));
    }

    default byte[] peekUntil(byte b, int maxLength) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        var index = indexOf(b, maxLength);
        return peek(toIntExact(index));
    }

    default byte[] peekUntil(byte[] b) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        var index = indexOf(b);
        return peek(toIntExact(index));
    }

    default byte[] peekUntil(byte[] b, int maxLength) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        var index = indexOf(b, maxLength);
        return peek(toIntExact(index));
    }

    default long inputStreamTransferTo(OutputStream out) throws IOException {
        return inputStreamTransferTo(out, Long.MAX_VALUE);
    }

}
