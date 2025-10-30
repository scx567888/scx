package cool.scx.http.media.byte_array;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;
import cool.scx.io.ByteInput;

import java.io.IOException;

/// 保持单例模式
///
/// @author scx567888
/// @version 0.0.1
public final class ByteArrayReader implements MediaReader<byte[]> {

    public static final ByteArrayReader BYTE_ARRAY_READER = new ByteArrayReader();

    private ByteArrayReader() {

    }

    @Override
    public byte[] read(ByteInput byteInput, ScxHttpHeaders headers) throws IOException {
        try (byteInput) {
            return byteInput.readAll();
        }
    }

}
