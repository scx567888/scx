package cool.scx.io.io_stream_source;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public class BytesSource implements InputStreamSource {

    private final byte[] bytes;

    public BytesSource(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public InputStream toInputStream() throws IOException {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void writeToOutputStream(OutputStream out) throws IOException {
        out.write(bytes);
    }

    @Override
    public byte[] toBytes() throws IOException {
        return bytes;
    }

    @Override
    public void toFile(Path outputPath, OpenOption... options) throws IOException {
        Files.write(outputPath, bytes, options);
    }

}
