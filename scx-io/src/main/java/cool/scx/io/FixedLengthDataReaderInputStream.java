package cool.scx.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class FixedLengthDataReaderInputStream extends InputStream {

    private final PowerfulLinkedDataReader dataReader;
    private final long maxLength;
    private long position;

    public FixedLengthDataReaderInputStream(PowerfulLinkedDataReader dataReader, long maxLength) {
        this.dataReader = dataReader;
        this.maxLength = maxLength;
        this.position = 0;
    }

    @Override
    public int read() {
        if (position >= maxLength) {
            return -1;
        }
        int i = dataReader.inputStreamRead();
        return movePosition(i);
    }

    @Override
    public int read(byte[] b, int off, int len) {
        if (position >= maxLength) {
            return -1;
        }
        var length = Math.min(len, maxLength - position);
        var i = dataReader.inputStreamRead(b, off, (int) length);
        return movePosition(i);
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        if (position >= maxLength) {
            return -1;
        }
        var length = maxLength - position;
        var i = dataReader.inputStreamTransferTo(out, length);
        return movePosition(i);
    }

    private int movePosition(int i) {
        if (i == -1) {
            return -1;
        }
        position = position + i;
        return i;
    }

    private long movePosition(long i) {
        if (i == -1) {
            return -1;
        }
        position = position + i;
        return i;
    }

}
