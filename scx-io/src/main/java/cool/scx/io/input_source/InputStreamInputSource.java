package cool.scx.io.input_source;

import cool.scx.io.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public class InputStreamInputSource implements InputSource {

    private final InputStream inputStream;

    public InputStreamInputSource(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public byte[] read(int length) throws IOException {
        return inputStream.readNBytes(length);
    }

    @Override
    public byte[] readAll() throws IOException {
        return inputStream.readAllBytes();
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        return inputStream.transferTo(out);
    }

    @Override
    public void transferTo(Path outputPath, OpenOption... options) throws IOException {
        try (var outputStream = Files.newOutputStream(outputPath, options)) {
            inputStream.transferTo(outputStream);
        }
    }

    @Override
    public InputStream toInputStream() {
        return inputStream;
    }

    @Override
    public void close() throws Exception {
        inputStream.close();
    }

}
