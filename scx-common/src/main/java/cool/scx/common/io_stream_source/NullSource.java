package cool.scx.common.io_stream_source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public class NullSource implements InputStreamSource {

    public NullSource() {

    }

    @Override
    public InputStream toInputStream() throws IOException {
        return InputStream.nullInputStream();
    }

    @Override
    public void writeToOutputStream(OutputStream out) throws IOException {

    }

    @Override
    public byte[] toBytes() throws IOException {
        return new byte[]{};
    }

    @Override
    public void toFile(Path outputPath, OpenOption... options) throws IOException {
        Files.createFile(outputPath);
    }

}
