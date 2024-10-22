package cool.scx.io;

import java.nio.ByteBuffer;

public class ScxIOHelper {

    public static int[] computeLPSArray(byte[] pattern) {
        int[] lps = new int[pattern.length];
        int length = 0;
        int i = 1;

        while (i < pattern.length) {
            if (pattern[i] == pattern[length]) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    public static byte[] toByteArray(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return bytes;
    }

    public static ByteBuffer enlargeBuffer(ByteBuffer buffer) {
        ByteBuffer largerBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
        buffer.flip();
        largerBuffer.put(buffer);
        return largerBuffer;
    }

    public static ByteBuffer ensureCapacity(ByteBuffer buffer, int additionalCapacity) {
        if (buffer.remaining() >= additionalCapacity) {
            return buffer;
        }
        ByteBuffer largerBuffer = ByteBuffer.allocate(buffer.capacity() * 2 + additionalCapacity);
        buffer.flip();
        largerBuffer.put(buffer);
        return largerBuffer;
    }


    public static ByteBuffer expandByteBuffer(ByteBuffer byteBuffer, int newCapacity) {
        if (newCapacity <= byteBuffer.capacity()) {
            throw new IllegalArgumentException("New capacity must be greater than current capacity.");
        }
        ByteBuffer newByteBuffer = ByteBuffer.allocate(newCapacity);
        ByteBuffer duplicate = byteBuffer.duplicate();
        boolean wasReadMode = isReadMode(duplicate);
        if (wasReadMode) {
            duplicate.clear(); // 如果是读模式，转换到写模式以复制所有内容
        }
        newByteBuffer.put(duplicate);
        if (wasReadMode) {
            newByteBuffer.flip(); // 恢复新缓冲区的读模式
        }
        newByteBuffer.position(byteBuffer.position()); // 恢复原缓冲区位置
        return newByteBuffer;
    }

    /** 扩容 (默认 2 倍) */
    public static ByteBuffer expandByteBuffer(ByteBuffer byteBuffer) {
        return expandByteBuffer(byteBuffer, byteBuffer.capacity() * 2);
    }

    public static boolean isReadMode(ByteBuffer buffer) {
        return buffer.position() == 0 && buffer.limit() == buffer.capacity();
    }

    public static boolean isWriteMode(ByteBuffer buffer) {
        return buffer.limit() == buffer.capacity() && buffer.position() < buffer.capacity();
    }


}
