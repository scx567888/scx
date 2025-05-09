package cool.scx.io.io_stream;

import cool.scx.io.data_reader.DataReader;
import cool.scx.io.data_reader.LinkedDataReader;
import cool.scx.io.data_supplier.DataSupplier;
import cool.scx.io.exception.NoMoreDataException;

import java.io.IOException;
import java.io.OutputStream;

public class DataReaderInputStream extends CheckedInputStream {

    private final DataReader dataReader;

    public DataReaderInputStream(DataReader dataReader) {
        this.dataReader = dataReader;
    }

    public DataReaderInputStream(DataSupplier dataSupplier) {
        this.dataReader = new LinkedDataReader(dataSupplier);
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

    public DataReader dataReader() {
        return dataReader;
    }

    @Override
    public void mark(int readlimit) {
        dataReader.mark();
    }

    @Override
    public void reset() throws IOException {
        dataReader.reset();
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        //模拟 inputStream 的行为 没数据 返回 0 字节 而不是抛出 NoMoreDataException
        try {
            return dataReader.read(len);
        } catch (NoMoreDataException e) {
            return new byte[0];
        }
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        //模拟 inputStream 的行为 没数据 返回 0 字节 而不是抛出 NoMoreDataException
        try {
            return dataReader.read(Integer.MAX_VALUE);
        } catch (NoMoreDataException e) {
            return new byte[0];
        }
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

}
