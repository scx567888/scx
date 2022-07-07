package cool.scx.vo;

import io.vertx.core.http.impl.MimeMapping;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * 文件下载 vo
 *
 * @author scx567888
 * @version 1.0.10
 */
public final class Download extends BaseWriter {

    private Download(InputStream inputStream, String downloadName) {
        super(inputStream, MimeMapping.getMimeTypeForFilename(downloadName.toLowerCase()), BaseVo.getDownloadContentDisposition(downloadName));
    }

    private Download(Path path, String downloadName) {
        super(path, MimeMapping.getMimeTypeForFilename(downloadName.toLowerCase()), BaseVo.getDownloadContentDisposition(downloadName));
    }

    private Download(byte[] bytes, String downloadName) {
        super(bytes, MimeMapping.getMimeTypeForFilename(downloadName.toLowerCase()), BaseVo.getDownloadContentDisposition(downloadName));
    }

    public static Download of(InputStream inputStream, String downloadName) {
        return new Download(inputStream, downloadName);
    }

    public static Download of(byte[] bytes, String downloadName) {
        return new Download(bytes, downloadName);
    }

    public static Download of(Path path, String downloadName) {
        return new Download(path, downloadName);
    }

    public static Download of(Path path) {
        return new Download(path, path.getFileName().toString());
    }

}
