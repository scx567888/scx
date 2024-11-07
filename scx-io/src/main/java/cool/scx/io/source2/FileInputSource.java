package cool.scx.io.source2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileInputSource extends InputStreamInputSource {

    private final Path path;

    public FileInputSource(Path path) throws IOException {
        super(Files.newInputStream(path));
        this.path = path;
    }

}
