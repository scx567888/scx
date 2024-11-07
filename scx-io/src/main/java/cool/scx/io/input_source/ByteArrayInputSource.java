package cool.scx.io.input_source;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayInputSource extends LazyInputStreamInputSource {

    private final byte[] bytes;

    public ByteArrayInputSource(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public InputStream toInputStream0() throws IOException {
        return new ByteArrayInputStream(bytes);
    }

}
