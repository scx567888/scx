package cool.scx.io.input_source;

import cool.scx.io.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public abstract class LazyInputStreamInputSource implements InputSource {

    private InputStream inputStream;

    @Override
    public byte[] read(int length) throws IOException {
        return toInputStream().readNBytes(length);
    }

    @Override
    public byte[] readAll() throws IOException {
        return toInputStream().readAllBytes();
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        return toInputStream().transferTo(out);
    }

    @Override
    public void transferTo(Path outputPath, OpenOption... options) throws IOException {
        try (var outputStream = Files.newOutputStream(outputPath, options)) {
            toInputStream().transferTo(outputStream);
        }
    }

    @Override
    public InputStream toInputStream() throws IOException {
        if (inputStream == null) {
            inputStream = toInputStream0();
        }
        return inputStream;
    }

    @Override
    public void close() throws Exception {
        toInputStream().close();
    }

    protected abstract InputStream toInputStream0() throws IOException;

}
