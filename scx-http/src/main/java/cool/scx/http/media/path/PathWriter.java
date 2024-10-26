package cool.scx.http.media.path;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.io.IOHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import static cool.scx.io.IOHelper.writeFileToOut;

public class PathWriter implements MediaWriter {

    private final Path path;
    private final long fileRealSize;
    private final long offset;
    private final long length;

    public PathWriter(Path path) {
        this.path = path;
        this.fileRealSize = IOHelper.getFileSize(path);
        this.offset = 0;
        this.length = fileRealSize;
    }

    public PathWriter(Path path, long offset, long length) {
        this.path = path;
        this.fileRealSize = IOHelper.getFileSize(path);
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
        try (outputStream) {
            if (offset == 0 && length == fileRealSize) {
                writeFileToOut(path, outputStream);
            } else {
                writeFileToOut(path, outputStream, offset, length);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
