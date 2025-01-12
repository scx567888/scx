package cool.scx.http.x.http1x;

import cool.scx.io.DataReader;
import cool.scx.io.PowerfulLinkedDataReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpChunkedInputStream extends InputStream {

    private final PowerfulLinkedDataReader dataReader;

    public HttpChunkedInputStream(DataReader dataReader, long maxLength, Runnable onFinish) {
        this.dataReader = new PowerfulLinkedDataReader(new HttpChunkedDataSupplier(dataReader, maxLength, onFinish));
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
