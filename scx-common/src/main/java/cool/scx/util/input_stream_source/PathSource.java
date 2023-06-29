package cool.scx.util.input_stream_source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

final class PathSource implements InputStreamSource {

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

}
