package cool.scx.io;

import java.nio.ByteBuffer;

import static cool.scx.io.SkipDataPuller.SKIP_DATA_PULLER;

public class PowerfulLinkedDataReader extends LinkedDataReader {

    public PowerfulLinkedDataReader(DataSupplier dataSupplier) {
        super(dataSupplier);
    }

    public PowerfulLinkedDataReader() {
        super();
    }

    /**
     * read 方法会持续阻塞直到获取到足够的数据或者数据提供器返回 null
     * 但在网络环境中 这种方式可能导致 无法正确读取数据
     * 此方法调用时仅仅会尝试 拉取一次数据 即使数据不足
     *
     * @param maxLength maxLength
     * @return b
     * @throws NoMoreDataException e
     */
    public byte[] fastRead(int maxLength) throws NoMoreDataException {
        //通过 计数数据拉取器限制只拉取一次
        var dp = new CountingDataPuller(dataPuller, 1);
        ensureAvailableOrThrow(dp);
        var consumer = new ByteArrayDataConsumer();
        walk(consumer, maxLength, true, dp);
        return consumer.getBytes();
    }

    public byte[] fastPeek(int maxLength) throws NoMoreDataException {
        var dp = new CountingDataPuller(dataPuller, 1);
        ensureAvailableOrThrow(dp);
        var consumer = new ByteArrayDataConsumer();
        walk(consumer, maxLength, false, dp);
        return consumer.getBytes();
    }

    /**
     * 和 fastRead 相比此方法仅仅会在缓冲数据为空时拉取一次
     *
     * @param maxLength a
     * @return a
     * @throws NoMoreDataException e
     */
    public byte[] tryRead(int maxLength) throws NoMoreDataException {
        var dp = new CountingDataPuller(dataPuller, 1);
        ensureAvailableOrThrow(dp);
        var consumer = new ByteArrayDataConsumer();
        walk(consumer, maxLength, true, SKIP_DATA_PULLER);
        return consumer.getBytes();
    }

    // InputStream 写法的 read
    public int tryRead(byte[] b, int off, int len) throws NoMoreDataException {
        var dp = new CountingDataPuller(dataPuller, 1);
        var b1 = ensureAvailable(dp);
        if (!b1) {
            return -1;
        }
        var consumer = new FillByteArrayDataConsumer(b, off, len);
        walk(consumer, len, true, SKIP_DATA_PULLER);
        return consumer.getFilledLength();
    }

    // ByteChannel 写法的 read
    public int tryRead(ByteBuffer b) throws NoMoreDataException {
        var dp = new CountingDataPuller(dataPuller, 1);
        var b1 = ensureAvailable(dp);
        if (!b1) {
            return -1;
        }
        var consumer = new FillByteBufferDataConsumer(b);
        walk(consumer, b.remaining(), true, SKIP_DATA_PULLER);
        return consumer.getFilledLength();
    }

    public byte[] tryPeek(int maxLength) throws NoMoreDataException {
        var dp = new CountingDataPuller(dataPuller, 1);
        ensureAvailableOrThrow(dp);
        var consumer = new ByteArrayDataConsumer();
        walk(consumer, maxLength, false, SKIP_DATA_PULLER);
        return consumer.getBytes();
    }

    public byte[] readUntil(KMPDataIndexer indexer, int max) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(indexer, max);
        var data = read(index);
        skip(indexer.pattern().length);
        return data;
    }

    public byte[] readUntil(KMPDataIndexer indexer) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(indexer, Integer.MAX_VALUE);
        var data = read(index);
        skip(indexer.pattern().length);
        return data;
    }

    public byte[] peekUntil(KMPDataIndexer indexer, int max) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(indexer, max);
        return peek(index);
    }

    public byte[] peekUntil(KMPDataIndexer indexer) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(indexer, Integer.MAX_VALUE);
        return peek(index);
    }

    /**
     * 为了极致的性能考虑 复用 KMPDataIndexer
     */
    public int indexOf(KMPDataIndexer indexer, int max) throws NoMatchFoundException, NoMoreDataException {
        ensureAvailableOrThrow();
        indexer.reset();
        return super.indexOf(indexer, max);
    }

}
