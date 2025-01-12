package cool.scx.http.x.http1x;

import cool.scx.io.ByteArrayDataSupplier;
import cool.scx.io.PowerfulLinkedDataReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 固定长度的 读取器
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
            return -1;
        }
        int i = dataReader.inputStreamRead();
        if (i == -1) {
            return -1;
        }
        position = position + 1;
        return i;
    }

    @Override
    public int read(byte[] b, int off, int len) {
        if (position >= maxLength) {
            return -1;
        }
        var length = Math.min(len, maxLength - position);
        var i = dataReader.inputStreamRead(b, off, (int) length);
        if (i == -1) {
            return -1;
        }
        position = position + i;
        return i;
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        if (position >= maxLength) {
            return -1;
        }
        var length = maxLength - position;
        var i = dataReader.inputStreamTransferTo(out, length);
        if (i == -1) {
            return -1;
        }
        position = position + i;
        return i;
    }

    private void completeRead() {
        if (!isFinished) {
            isFinished = true;
            onFinish.run();
        }
    }

    public static void main(String[] args) {
        var s = new PowerfulLinkedDataReader(new ByteArrayDataSupplier("Hello World".getBytes()));
        var ss = new FixedLengthInputStream(s, 1, () -> {
            System.out.println("完成了");
        });
        var i = 0;
        while ((i = ss.read()) != -1) {
            System.out.println((char) i);
        }
    }

}
