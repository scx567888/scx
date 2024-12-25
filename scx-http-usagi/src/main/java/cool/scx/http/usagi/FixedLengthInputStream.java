package cool.scx.http.usagi;

import cool.scx.io.data_reader.DataReader;
import cool.scx.io.exception.NoMoreDataException;

import java.io.InputStream;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class FixedLengthInputStream extends InputStream {

    private final DataReader dataReader;
    private final long maxLength;
    private int position;

    public FixedLengthInputStream(DataReader dataReader, long maxLength) {
        this.dataReader = dataReader;
        this.maxLength = maxLength;
        this.position = 0;
    }

    @Override
    public int read() {
        if (position >= maxLength) {
            return -1;
        }
        try {
            position = position + 1;
            return dataReader.read() & 0xFF;
        } catch (NoMoreDataException e) {
            return -1;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) {
        if (position >= maxLength) {
            return -1;
        }
        try {
            var s = dataReader.read((int) Math.min(len, maxLength - position));
            System.arraycopy(s, 0, b, off, s.length);
            position += s.length;
            return s.length;
        } catch (NoMoreDataException e) {
            return -1;
        }
    }

}
