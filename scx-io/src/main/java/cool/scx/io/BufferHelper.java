package cool.scx.io;

import java.nio.ByteBuffer;

public class BufferHelper {

    public static byte[] toBytes(ByteBuffer buffer) {
        var bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return bytes;
    }

    public static ByteBuffer expandBuffer(ByteBuffer oldBuffer) {
        var newBuffer = ByteBuffer.allocate(oldBuffer.capacity() * 2);
        oldBuffer.flip();
        newBuffer.put(oldBuffer);
        return newBuffer;
    }

}
