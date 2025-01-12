package cool.scx.http.x.http1x;

import cool.scx.io.PowerfulLinkedDataReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 固定长度的 读取器 todo 性能待优化
 *
 * @author scx567888
 * @version 0.0.2
 */
public class FixedLengthInputStream extends InputStream {

    private final PowerfulLinkedDataReader dataReader;
    private final long maxLength;
    private long position;
    private final Runnable onFinish;
    private boolean isFinished;

    public FixedLengthInputStream(PowerfulLinkedDataReader dataReader, long maxLength) {
        this(dataReader, maxLength, () -> {});
    }

    public FixedLengthInputStream(PowerfulLinkedDataReader dataReader, long maxLength, Runnable onFinish) {
        this.dataReader = dataReader;
        this.maxLength = maxLength;
        this.position = 0;
        this.isFinished = false;
        this.onFinish = onFinish;
    }

    @Override
    public int read() {
        if (position >= maxLength) {
            completeRead();
            return -1;
        }
        int i = dataReader.inputStreamRead();
        if (i == -1) {
            completeRead();
            return -1;
        }
        position = position + 1;
        if (position >= maxLength) {
            completeRead();
        }
        return i;
    }

    @Override
    public int read(byte[] b, int off, int len) {
        if (position >= maxLength) {
            completeRead();
            return -1;
        }
        var length = Math.min(len, maxLength - position);
        var i = dataReader.inputStreamRead(b, off, (int) length);
        if (i == -1) {
            completeRead();
            return -1;
        }
        position = position + i;
        if (position >= maxLength) {
            completeRead();
        }
        return i;
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        if (position >= maxLength || isFinished) {
            completeRead();
            return -1;
        }
        var length = maxLength - position;
        var i = dataReader.inputStreamTransferTo(out, length);
        if (i == -1) {
            completeRead();
            return -1;
        }
        position = position + i;
        if (position >= maxLength) {
            completeRead();
        }
        return i;
    }

    private void completeRead() {
        if (!isFinished) {
            isFinished = true;
            onFinish.run();
        }
    }

}
