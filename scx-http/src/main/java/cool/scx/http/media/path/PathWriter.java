package cool.scx.http.media.path;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media_type.FileFormat;
import cool.scx.io.IOHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import static cool.scx.http.media_type.MediaType.APPLICATION_OCTET_STREAM;
import static cool.scx.io.IOHelper.writeFileToOut;

/// PathWriter
///
/// @author scx567888
/// @version 0.0.1
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
    public long beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        // 这里如果用户没有指定格式的话 我们尝试猜测一下 
        if (responseHeaders.contentType() == null) {
            var fileFormat = FileFormat.findByFileName(path.toString());
            if (fileFormat == null) { //没找到就使用 二进制流
                responseHeaders.contentType(APPLICATION_OCTET_STREAM);
            } else {
                responseHeaders.contentType(fileFormat.mediaType());
            }
        }
        return length;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        try (outputStream) {
            //判断一下是不是发送整个文件
            var writeFullFile = offset == 0 && length == fileRealSize;
            if (writeFullFile) {
                writeFileToOut(path, outputStream);
            } else {
                writeFileToOut(path, outputStream, offset, length);
            }
        }
    }

}
