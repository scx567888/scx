package cool.scx.http.media.path;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

/**
 * 将内容写入到文件
 */
public class PathReader implements MediaReader<Path> {

    private final Path path;
    private final OpenOption[] options;

    /**
     * 写入的路径
     *
     * @param path a
     */
    public PathReader(Path path, OpenOption... options) {
        this.path = path;
        this.options = options;
    }

    @Override
    public Path read(InputStream inputStream, ScxHttpHeaders headers) {
        try {
            var outputStream = Files.newOutputStream(path, options);
            inputStream.transferTo(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }

}
