package cool.scx.http.usagi;

import cool.scx.io.LinkedDataReader;
import cool.scx.io.PowerfulLinkedDataReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * HttpChunkedInputStream
 *
 * @author scx567888
 * @version 0.0.1
 */
public class HttpChunkedInputStream extends InputStream {

    private final PowerfulLinkedDataReader chunkedDataReader;

    public HttpChunkedInputStream(LinkedDataReader dataReader) {
        this.chunkedDataReader = new PowerfulLinkedDataReader(new HttpChunkedDataSupplier(dataReader));
    }

    @Override
    public int read() {
        return chunkedDataReader.inputStreamRead();
    }

    @Override
    public int read(byte[] b, int off, int len) {
        return chunkedDataReader.inputStreamRead(b, off, len);
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        return chunkedDataReader.inputStreamTransferTo(out);
    }

}
