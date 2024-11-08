package cool.scx.io.input_source;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileInputSource extends LazyInputStreamInputSource {

    private final Path path;

    public FileInputSource(Path path) {
        this.path = path;
    }

    @Override
    public InputStream toInputStream0() throws IOException {
        return Files.newInputStream(path);
    }

}
