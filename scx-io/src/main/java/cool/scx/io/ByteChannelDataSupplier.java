package cool.scx.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.function.Supplier;

import static cool.scx.io.ScxIOHelper.toByteArray;

public class ByteChannelDataSupplier implements Supplier<byte[]> {

    private static final int BUFFER_LENGTH = 8 * 1024;
    private final ByteChannel dataChannel;
    private final ByteBuffer readBuffer;

    public ByteChannelDataSupplier(ByteChannel dataChannel) {
        this(dataChannel, BUFFER_LENGTH);
    }

    public ByteChannelDataSupplier(ByteChannel dataChannel, int bufferLength) {
        this.dataChannel = dataChannel;
        this.readBuffer = ByteBuffer.allocate(bufferLength);
    }

    @Override
    public byte[] get() {
        try {
            readBuffer.clear();
            int bytesRead = dataChannel.read(readBuffer);
            if (bytesRead == -1) {
                return null; // end of data
            }
            readBuffer.flip();
            return toByteArray(readBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
