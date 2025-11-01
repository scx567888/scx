package cool.scx.http.media.path;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;
import cool.scx.io.ByteInput;
import cool.scx.io.adapter.ByteInputAdapter;
import cool.scx.io.exception.AlreadyClosedException;
import cool.scx.io.exception.ScxIOException;

import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import static cool.scx.common.util.IOUtils.readInToFile;

/// 将内容写入到文件
/// todo 这里是否有必要支持 复杂的写入 比如指定文件的偏移量和写入长度 还是保持当前类的简便化 因为 用户可以直接拿到 InputStream 添加复杂的功能是否有意义 ?
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
    public Path read(ByteInput byteInput, ScxHttpHeaders headers) throws ScxIOException, AlreadyClosedException {
        try (byteInput) {
            // todo 应该有更好方式实现
            readInToFile(ByteInputAdapter.byteInputToInputStream(byteInput), path, options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //这里直接返回 path 方便用户链式调用
        return path;
    }

}
