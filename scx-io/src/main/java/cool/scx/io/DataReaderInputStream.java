package cool.scx.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataReaderInputStream extends InputStream {

    private final PowerfulLinkedDataReader dataReader;

    public DataReaderInputStream(PowerfulLinkedDataReader dataReader) {
        this.dataReader = dataReader;
    }

    public DataReaderInputStream(DataSupplier dataSupplier) {
        this.dataReader = new PowerfulLinkedDataReader(dataSupplier);
    }

    @Override
    public int read() throws IOException {
        return dataReader.inputStreamRead();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return dataReader.inputStreamRead(b, off, len);
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        return dataReader.inputStreamTransferTo(out);
    }

}
