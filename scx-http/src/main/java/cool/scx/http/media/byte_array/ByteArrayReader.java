package cool.scx.http.media.byte_array;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.IOException;
import java.io.InputStream;

/// 保持单例模式
///
/// @author scx567888
/// @version 0.0.1
public final class ByteArrayReader implements MediaReader<byte[]> {

    public static final ByteArrayReader BYTE_ARRAY_READER = new ByteArrayReader();

    private ByteArrayReader() {

    }

    @Override
    public byte[] read(InputStream inputStream, ScxHttpHeaders headers) throws IOException {
        try (inputStream) {
            return inputStream.readAllBytes();
        }
    }

}
