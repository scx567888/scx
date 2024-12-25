package cool.scx.http.media.multi_part;

import cool.scx.common.util.RandomUtils;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.io.NoMatchFoundException;
import cool.scx.io.OutputStreamDataConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

/**
 * MultiPartStreamCached
 *
 * @author scx567888
 * @version 0.0.1
 */
public class MultiPartStreamCached extends MultiPartStream {

    private final Path cachePath;

    public MultiPartStreamCached(InputStream inputStream, String boundary, Path cachePath) {
        super(inputStream, boundary);
        this.cachePath = cachePath;
    }

    public static boolean needCached(ScxHttpHeaders headers) {
        //根据是否存在文件名来假定上传的不是文件 只有文件才缓存
        var contentDisposition = headers.contentDisposition();
        if (contentDisposition == null) {
            return false;
        }
        var filename = contentDisposition.filename();
        return filename != null;
    }

    public Path readContentToPath(Path path) throws IOException {
        //保证一定有目录
        Files.createDirectories(path.getParent());
        var output = Files.newOutputStream(path);

        try (output) {
            //我们需要查找终结点 先假设不是最后一个 那我们就需要查找下一个开始位置 
            try {
                var i = linkedDataReader.indexOf(boundaryBytes);
                // i - 2 因为我们不需要读取内容结尾的 \r\n  
                linkedDataReader.read(new OutputStreamDataConsumer(output), i - 2);
                //跳过 \r\n 方便后续读取
                linkedDataReader.skip(2);
            } catch (NoMatchFoundException e) {
                // 理论上一个正常的 MultiPart 不会有这种情况
                throw new RuntimeException("异常状态 !!!");
            }
        }

        return path;
    }

    @Override
    public MultiPartPart next() {
        if (!hasNextPart) {
            throw new NoSuchElementException("No more parts available.");
        }
        try {

            var part = new MultiPartPartImpl();

            // 读取当前部分的头部信息
            var headers = readToHeaders();
            part.headers(headers);

            var b = needCached(headers);
            if (b) {
                var contentPath = readContentToPath(cachePath.resolve(RandomUtils.randomString(32)));
                part.body(contentPath);
            } else {
                var content = readContentToByte();
                part.body(content);
            }

            // 检查是否有下一个部分
            hasNextPart = readNext();

            return part;
        } catch (IOException e) {
            throw new RuntimeException("Error reading next part", e);
        }
    }

}
