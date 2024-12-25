package cool.scx.http.usagi;

import cool.scx.io.LinkedDataReader;
import cool.scx.io.NoMoreDataException;

import java.io.InputStream;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class HttpChunkedInputStream extends InputStream {

    private final LinkedDataReader chunkedDataReader;

    public HttpChunkedInputStream(LinkedDataReader dataReader) {
        this.chunkedDataReader = new LinkedDataReader(new HttpChunkedDataSupplier(dataReader));
    }

    @Override
    public int read() {
        try {
            return chunkedDataReader.read() & 0xFF;
        } catch (NoMoreDataException e) {
            return -1;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) {
        try {
            var data = chunkedDataReader.read(len);
            System.arraycopy(data, 0, b, off, data.length);
            return data.length;
        } catch (NoMoreDataException e) {
            return -1;
        }
    }

}
