package cool.scx.io;

public class IOHelper {

    public static byte[] compressBytes(byte[] bytes, int offset, int length) {
        if (offset == 0 && length == bytes.length) {
            return bytes;
        }
        var data = new byte[length];
        System.arraycopy(bytes, offset, data, 0, length);
        return data;
    }

}
