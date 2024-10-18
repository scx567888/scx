package cool.scx.io;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Supplier;

public class DataChannelDataSupplier implements Supplier<byte[]> {

    private static final int BUFFER_LENGTH = 8 * 1024;
    private final DataChannel dataChannel;
    private final byte[] readBuffer;

    public DataChannelDataSupplier(DataChannel dataChannel) {
        this.dataChannel = dataChannel;
        this.readBuffer = new byte[BUFFER_LENGTH];
    }

    @Override
    public byte[] get() {
        try {
            int bytesRead = dataChannel.read(readBuffer);
            if (bytesRead == -1) {
                return null; // end of data
            }
            return Arrays.copyOf(readBuffer, bytesRead);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
