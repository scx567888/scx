package cool.scx.io.data_reader;

import cool.scx.io.data_consumer.FillByteArrayDataConsumer;
import cool.scx.io.data_consumer.OutputStreamDataConsumer;
import cool.scx.io.data_supplier.DataSupplier;

import java.io.OutputStream;

public class PowerfulLinkedDataReader extends LinkedDataReader {

    public PowerfulLinkedDataReader(DataSupplier dataSupplier) {
        super(dataSupplier);
    }

    public PowerfulLinkedDataReader() {
        super();
    }

    // InputStream 写法的 read
    public int inputStreamRead() {
        var r = ensureAvailable();
        if (!r) {
            return -1;
        }
        var b = head.bytes[head.position];
        head.position = head.position + 1;
        return b & 0xFF;
    }

    // InputStream 写法的 read
    public int inputStreamRead(byte[] b, int off, int len) {
        if (len > 0) {
            var r = ensureAvailable();
            if (!r) {
                return -1;
            }
        }
        var consumer = new FillByteArrayDataConsumer(b, off, len);
        walk(consumer, len, true);
        return consumer.getFilledLength();
    }

    // InputStream 写法的 read
    public long inputStreamTransferTo(OutputStream out, long maxLength) {
        if (maxLength > 0) {
            var r = ensureAvailable();
            if (!r) {
                return -1;
            }
        }
        var consumer = new OutputStreamDataConsumer(out);
        walk(consumer, maxLength, true);
        return consumer.byteCount();
    }

    // InputStream 写法的 read
    public long inputStreamTransferTo(OutputStream out) {
        return inputStreamTransferTo(out, Long.MAX_VALUE);
    }

}
