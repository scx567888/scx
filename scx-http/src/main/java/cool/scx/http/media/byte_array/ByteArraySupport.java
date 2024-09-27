package cool.scx.http.media.byte_array;

import cool.scx.http.media.MediaReader;
import cool.scx.http.media.MediaSupport;

import static cool.scx.http.media.byte_array.ByteArrayReader.BYTE_ARRAY_READER;

/**
 * 保持单例模式
 */
public class ByteArraySupport implements MediaSupport<byte[]> {

    public static final ByteArraySupport BYTE_ARRAY_SUPPORT = new ByteArraySupport();

    private ByteArraySupport() {
    }

    @Override
    public MediaReader<byte[]> reader() {
        return BYTE_ARRAY_READER;
    }

}
