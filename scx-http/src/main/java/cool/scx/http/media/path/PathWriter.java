package cool.scx.http.media.path;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;

import java.io.OutputStream;
import java.nio.file.Path;

import static cool.scx.http.media.path.PathHelper.fileCopy;
import static cool.scx.http.media.path.PathHelper.fileCopyWithOffset;

public class PathWriter implements MediaWriter {

    private final Path path;
    private final long fileRealSize;
    private final long offset;
    private final long length;

    public PathWriter(Path path) {
        this.path = path;
        this.fileRealSize = PathHelper.getFileSize(path);
        this.offset = 0;
        this.length = fileRealSize;
    }

    public PathWriter(Path path, long offset, long length) {
        this.path = path;
        this.fileRealSize = PathHelper.getFileSize(path);
        this.offset = offset;
        this.length = length;
    }

    @Override
    public void beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        //如果已经设置了 contentLength 我们跳过
        if (requestHeaders.contentLength() == null) {
            responseHeaders.contentLength(length);
        }
    }

    @Override
    public void write(OutputStream outputStream) {
        if (offset == 0 && length == fileRealSize) {
            fileCopy(path, outputStream);
        } else {
            fileCopyWithOffset(path, outputStream, offset, length);
        }
    }

}
