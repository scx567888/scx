package cool.scx.io;

import java.nio.ByteBuffer;

public class ByteBufferHelper {

    public static byte[] toByteArray(ByteBuffer buffer) {
        var bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return bytes;
    }

}
