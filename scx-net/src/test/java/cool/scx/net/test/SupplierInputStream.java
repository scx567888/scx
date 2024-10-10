package cool.scx.net.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

public class SupplierInputStream extends InputStream {
    private final Supplier<byte[]> supplier;
    private final Queue<Byte> bufferQueue = new LinkedList<>();
    private boolean endOfStream = false;

    public SupplierInputStream(Supplier<byte[]> supplier) {
        this.supplier = supplier;
    }

    @Override
    public int read() throws IOException {
        if (bufferQueue.isEmpty() && !endOfStream) {
            byte[] data = supplier.get();
            if (data == null || data.length == 0) {
                endOfStream = true;
            } else {
                for (byte b : data) {
                    bufferQueue.add(b);
                }
            }
        }

        return bufferQueue.isEmpty() ? -1 : bufferQueue.poll();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (bufferQueue.isEmpty() && !endOfStream) {
            byte[] data = supplier.get();
            if (data == null || data.length == 0) {
                endOfStream = true;
            } else {
                for (byte datum : data) {
                    bufferQueue.add(datum);
                }
            }
        }

        if (bufferQueue.isEmpty()) {
            return -1;
        }

        int bytesRead = 0;
        for (int i = off; i < off + len && !bufferQueue.isEmpty(); i++) {
            b[i] = bufferQueue.poll();
            bytesRead++;
        }

        return bytesRead;
    }
}
