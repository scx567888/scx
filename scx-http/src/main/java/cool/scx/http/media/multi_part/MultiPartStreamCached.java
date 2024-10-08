package cool.scx.http.media.multi_part;

import cool.scx.common.util.RandomUtils;
import cool.scx.http.ScxHttpHeaders;
import org.apache.commons.fileupload2.core.MultipartInput;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static cool.scx.http.media.multi_part.MultiPartStream.readContentToByte;
import static cool.scx.http.media.multi_part.MultiPartStream.readToHeaders;

public class MultiPartStreamCached implements MultiPart, Iterator<MultiPartPart> {

    private final MultipartInput multipartStream;
    private final Path cachePath;
    private boolean hasNextPart;
    private String boundary;

    public MultiPartStreamCached(InputStream inputStream, String boundary, Path cachePath) {
        this.cachePath = cachePath;
        var boundaryBytes = boundary.getBytes();
        try {
            this.multipartStream = MultipartInput.builder().setInputStream(inputStream).setBoundary(boundaryBytes).get();
            hasNextPart = multipartStream.skipPreamble();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public static Path readContentToPath(MultipartInput multipartStream, Path path) throws IOException {
        //保证一定有目录
        Files.createDirectories(path.getParent());
        var output = Files.newOutputStream(path);
        multipartStream.readBodyData(output);
        return path;
    }

    @Override
    public String boundary() {
        return boundary;
    }

    @Override
    public Iterator<MultiPartPart> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return hasNextPart;
    }

    @Override
    public MultiPartPart next() {
        if (!hasNextPart) {
            throw new NoSuchElementException("No more parts available.");
        }
        try {

            // 读取当前部分的头部信息
            var headers = readToHeaders(multipartStream);

            var part = new MultiPartPartImpl().headers(headers);

            var b = needCached(headers);
            if (b) {
                var contentPath = readContentToPath(multipartStream, cachePath.resolve(RandomUtils.randomString(32)));
                part.body(contentPath);
            } else {
                var content = readContentToByte(multipartStream);
                part.body(content);
            }
            // 检查是否有下一个部分
            hasNextPart = multipartStream.readBoundary();

            return part;
        } catch (IOException e) {
            throw new RuntimeException("Error reading next part", e);
        }
    }

}
