package cool.scx.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.function.Supplier;

public class DataChannelDataSupplier implements Supplier<byte[]> {

    private static final int BUFFER_LENGTH = 8 * 1024;
    private final DataChannel dataChannel;
    private final ByteBuffer readBuffer;

    public DataChannelDataSupplier(DataChannel dataChannel) {
        this.dataChannel = dataChannel;
        this.readBuffer = ByteBuffer.allocate(BUFFER_LENGTH);
    }

    @Override
    public byte[] get() {
        try {
            readBuffer.clear();
            int bytesRead = dataChannel.read(readBuffer);
            if (bytesRead == -1) {
                return null; // end of data
            }
            return Arrays.copyOf(readBuffer.array(), bytesRead);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
