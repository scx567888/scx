package cool.scx.io;

import java.io.OutputStream;
import java.nio.ByteBuffer;

import static java.lang.Math.toIntExact;

public class PowerfulLinkedDataReader extends LinkedDataReader {

    public PowerfulLinkedDataReader(DataSupplier dataSupplier) {
        super(dataSupplier);
    }

    public PowerfulLinkedDataReader() {
        super();
    }

    // InputStream 写法的 read
    public int inputStreamRead() throws NoMoreDataException {
        var r = ensureAvailable();
        if (!r) {
            return -1;
        }
        var b = head.bytes[head.position];
        head.position = head.position + 1;
        return b;
    }

    // InputStream 写法的 read
    public int inputStreamRead(byte[] b, int off, int len) throws NoMoreDataException {
        var r = ensureAvailable();
        if (!r) {
            return -1;
        }
        var consumer = new FillByteArrayDataConsumer(b, off, len);
        walk(consumer, len, true);
        return consumer.getFilledLength();
    }

    // InputStream 写法的 read
    public long inputStreamTransferTo(OutputStream out) throws NoMoreDataException {
        var r = ensureAvailable();
        if (!r) {
            return -1;
        }
        var consumer = new OutputStreamDataConsumer(out);
        walk(consumer, Long.MAX_VALUE, true);
        return consumer.byteCount();
    }

    // ByteChannel 写法的 read
    public int byteChannelRead(ByteBuffer b) throws NoMoreDataException {
        var r = ensureAvailable();
        if (!r) {
            return -1;
        }
        var consumer = new FillByteBufferDataConsumer(b);
        walk(consumer, b.remaining(), true);
        return consumer.getFilledLength();
    }

    public byte[] readUntil(KMPDataIndexer indexer, int max) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(indexer, max);
        var data = read(toIntExact(index));
        skip(indexer.pattern().length);
        return data;
    }

    public byte[] readUntil(KMPDataIndexer indexer) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(indexer, Long.MAX_VALUE);
        var data = read(toIntExact(index));
        skip(indexer.pattern().length);
        return data;
    }

    public byte[] peekUntil(KMPDataIndexer indexer, int max) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(indexer, max);
        return peek(toIntExact(index));
    }

    public byte[] peekUntil(KMPDataIndexer indexer) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(indexer, Long.MAX_VALUE);
        return peek(toIntExact(index));
    }

    /**
     * 为了极致的性能考虑 复用 KMPDataIndexer
     */
    public long indexOf(KMPDataIndexer indexer, long max) throws NoMatchFoundException, NoMoreDataException {
        ensureAvailableOrThrow();
        indexer.reset();
        return super.indexOf(indexer, max);
    }

}
