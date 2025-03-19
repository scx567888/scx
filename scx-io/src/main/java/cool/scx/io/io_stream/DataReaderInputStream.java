package cool.scx.io.io_stream;

import cool.scx.io.data_reader.PowerfulLinkedDataReader;
import cool.scx.io.data_supplier.DataSupplier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataReaderInputStream extends InputStream {

    private final PowerfulLinkedDataReader dataReader;
    private volatile boolean closed;

    public DataReaderInputStream(PowerfulLinkedDataReader dataReader) {
        this.dataReader = dataReader;
    }

    public DataReaderInputStream(DataSupplier dataSupplier) {
        this.dataReader = new PowerfulLinkedDataReader(dataSupplier);
    }

    private void ensureOpen() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
    }

    @Override
    public int read() throws IOException {
        ensureOpen();
        return dataReader.inputStreamRead();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        ensureOpen();
        return dataReader.inputStreamRead(b, off, len);
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        ensureOpen();
        return dataReader.inputStreamTransferTo(out);
    }

    public PowerfulLinkedDataReader dataReader() {
        return dataReader;
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

}
