package cool.scx.io;

import java.nio.ByteBuffer;

public class BufferHelper {

    public static byte[] toBytes(ByteBuffer buffer) {
        var bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return bytes;
    }

    public static ByteBuffer expandBuffer(ByteBuffer oldBuffer) {
        return expandBuffer(oldBuffer, oldBuffer.capacity() * 2);
    }

    public static ByteBuffer expandBuffer(ByteBuffer oldBuffer, int size) {
        var newBuffer = ByteBuffer.allocate(size);
        oldBuffer.flip();
        newBuffer.put(oldBuffer);
        return newBuffer;
    }

    /**
     * 会自动扩容
     *
     * @param buffer b
     * @param bytes  b
     * @return b
     */
    public static ByteBuffer putBytes(ByteBuffer buffer, byte[] bytes) {
        if (buffer.remaining() < bytes.length) {
            int newCapacity = Math.max(buffer.capacity() * 2, buffer.capacity() + bytes.length);
            buffer = expandBuffer(buffer, newCapacity);
        }
        buffer.put(bytes);
        return buffer;
    }

}
