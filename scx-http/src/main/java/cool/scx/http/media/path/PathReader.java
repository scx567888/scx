package cool.scx.http.media.path;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import static cool.scx.io.IOHelper.readInToFile;

/// 将内容写入到文件
///
/// @author scx567888
/// @version 0.0.1
public class PathReader implements MediaReader<Path> {

    private final Path path;
    private final OpenOption[] options;

    /// 写入的路径
    ///
    /// @param path a
    public PathReader(Path path, OpenOption... options) {
        this.path = path;
        this.options = options;
    }

    @Override
    public Path read(InputStream inputStream, ScxHttpHeaders headers) {
        try (inputStream) {
            readInToFile(inputStream, path, options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }

}
