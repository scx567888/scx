package cool.scx.http.peach;

import cool.scx.http.ScxHttpBody;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.io.LinkedDataReader;

import java.io.IOException;
import java.io.InputStream;

public class PeachScxHttpBody implements ScxHttpBody {

    private long position;
    private final LinkedDataReader dataReader;
    private final long contentLength;
    private final ScxHttpHeaders headers;
    private final InputStream inputStream;

    public PeachScxHttpBody(LinkedDataReader dataReader, ScxHttpHeaders headers, long contentLength) {
        this.dataReader = dataReader;
        this.headers = headers;
        this.contentLength = contentLength;
        this.inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                if (position >= contentLength) {
                    return -1;
                }
                position++;
                return dataReader.read() & 0xFF;
            }

            @Override
            public int read(byte[] b) throws IOException {
                if (position >= contentLength) {
                    return -1;
                }
                var s = dataReader.read(b.length);
                System.arraycopy(s, 0, b, 0, b.length);
                position += s.length;
                return s.length;
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                if (position >= contentLength) {
                    return -1;
                }
                var s = dataReader.read((int) Math.min(len, contentLength - position));
                System.arraycopy(s, 0, b, off, s.length);
                position += s.length;
                return s.length;
            }

        };
    }

    @Override
    public InputStream inputStream() {
        return inputStream;
    }

    @Override
    public ScxHttpHeaders headers() {
        return headers;
    }

}
