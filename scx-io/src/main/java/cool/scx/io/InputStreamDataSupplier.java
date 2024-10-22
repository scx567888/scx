package cool.scx.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.function.Supplier;

public class InputStreamDataSupplier implements Supplier<byte[]> {

    private static final int BUFFER_LENGTH = 8 * 1024;
    private final byte[] readBuffer;
    private final InputStream inputStream;

    public InputStreamDataSupplier(InputStream inputStream) {
        this(inputStream, BUFFER_LENGTH);
    }

    public InputStreamDataSupplier(InputStream inputStream, int bufferLength) {
        this.inputStream = inputStream;
        this.readBuffer = new byte[bufferLength];
    }

    @Override
    public byte[] get() {
        try {
            int r = inputStream.read(readBuffer);
            if (r == -1) {
                return null; // end of data
            }
            return Arrays.copyOf(readBuffer, r);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
