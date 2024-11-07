package cool.scx.io.io_source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public class PathSource implements InputSource {

    private final Path path;

    public PathSource(Path path) {
        this.path = path;
    }

    @Override
    public InputStream toInputStream() throws IOException {
        return Files.newInputStream(path);
    }

    @Override
    public void writeToOutputStream(OutputStream out) throws IOException {
        Files.copy(path, out);
    }

    @Override
    public void toFile(Path outputPath, OpenOption... options) throws IOException {
        Files.copy(path, outputPath);
    }

    @Override
    public byte[] toBytes() throws IOException {
        return Files.readAllBytes(path);
    }

}
