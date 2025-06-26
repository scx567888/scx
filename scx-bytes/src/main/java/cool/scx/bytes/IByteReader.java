package cool.scx.bytes;

import cool.scx.bytes.consumer.ByteArrayByteConsumer;
import cool.scx.bytes.consumer.ByteConsumer;
import cool.scx.bytes.exception.ByteSupplierException;
import cool.scx.bytes.exception.NoMatchFoundException;
import cool.scx.bytes.exception.NoMoreDataException;
import cool.scx.bytes.indexer.ByteIndexer;
import cool.scx.bytes.indexer.KMPByteIndexer;
import cool.scx.bytes.indexer.SingleByteIndexer;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.bytes.consumer.SkipByteConsumer.SKIP_BYTE_CONSUMER;
import static java.lang.Math.toIntExact;

/// IByteReader
///
/// @author scx567888
/// @version 0.0.1
public interface IByteReader {

    byte read() throws NoMoreDataException, ByteSupplierException;

    /// @param maxPullCount 底层最大拉取次数
    <E extends Throwable> void read(ByteConsumer<E> byteConsumer, long maxLength, long maxPullCount) throws NoMoreDataException, ByteSupplierException, E;

    byte peek() throws NoMoreDataException, ByteSupplierException;

    /// @param maxPullCount 底层最大拉取次数
    <E extends Throwable> void peek(ByteConsumer<E> byteConsumer, long maxLength, long maxPullCount) throws NoMoreDataException, ByteSupplierException, E;

    /// @param maxPullCount 底层最大拉取次数
    long indexOf(ByteIndexer indexer, long maxLength, long maxPullCount) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException;

    void mark();

    void reset();

    int inputStreamRead() throws IOException;

    int inputStreamRead(byte[] b, int off, int len) throws IOException;

    long inputStreamTransferTo(OutputStream out, long maxLength) throws IOException;

    byte[] inputStreamReadNBytes(long len) throws IOException;

    default byte[] read(int maxLength) throws NoMoreDataException, ByteSupplierException {
        return read(maxLength, Long.MAX_VALUE);
    }

    default byte[] read(int maxLength, long maxPullCount) throws NoMoreDataException, ByteSupplierException {
        var consumer = new ByteArrayByteConsumer();
        read(consumer, maxLength, maxPullCount);
        return consumer.bytes();
    }

    default <E extends Throwable> void read(ByteConsumer<E> byteConsumer, long maxLength) throws NoMoreDataException, ByteSupplierException, E {
        read(byteConsumer, maxLength, Long.MAX_VALUE);
    }

    default byte[] peek(int maxLength) throws NoMoreDataException, ByteSupplierException {
        return peek(maxLength, Long.MAX_VALUE);
    }

    default byte[] peek(int maxLength, long maxPullCount) throws NoMoreDataException, ByteSupplierException {
        var consumer = new ByteArrayByteConsumer();
        peek(consumer, maxLength, maxPullCount);
        return consumer.bytes();
    }

    default <E extends Throwable> void peek(ByteConsumer<E> byteConsumer, long maxLength) throws NoMoreDataException, ByteSupplierException, E {
        peek(byteConsumer, maxLength, Long.MAX_VALUE);
    }

    default void skip(long length) throws NoMoreDataException, ByteSupplierException {
        skip(length, Long.MAX_VALUE);
    }

    default void skip(long length, long maxPullCount) throws NoMoreDataException, ByteSupplierException {
        read(SKIP_BYTE_CONSUMER, length, maxPullCount);
    }

    default long indexOf(byte b) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        return indexOf(b, Long.MAX_VALUE);
    }

    default long indexOf(byte b, long maxLength) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        return indexOf(b, maxLength, Long.MAX_VALUE);
    }

    default long indexOf(byte b, long maxLength, long maxPullCount) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        return indexOf(new SingleByteIndexer(b), maxLength, maxPullCount);
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
        var bytes = read(toIntExact(index));
        skip(1);
        return bytes;
    }

    default byte[] readUntil(byte b, int maxLength) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        var index = indexOf(b, maxLength);
        var bytes = read(toIntExact(index));
        skip(1);
        return bytes;
    }

    default byte[] readUntil(byte[] b) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        var index = indexOf(b);
        var bytes = read(toIntExact(index));
        skip(b.length);
        return bytes;
    }

    default byte[] readUntil(byte[] b, int maxLength) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
        var index = indexOf(b, maxLength);
        var bytes = read(toIntExact(index));
        skip(b.length);
        return bytes;
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
