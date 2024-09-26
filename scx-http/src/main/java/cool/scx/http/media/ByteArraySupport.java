package cool.scx.http.media;

import cool.scx.http.ScxHttpServerRequestHeaders;

import java.io.IOException;
import java.io.InputStream;

public class ByteArraySupport implements MediaSupport<byte[]> {

    public static final ByteArraySupport BYTE_ARRAY_SUPPORT = new ByteArraySupport();

    @Override
    public byte[] read(InputStream inputStream, ScxHttpServerRequestHeaders headers) {
        try {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
