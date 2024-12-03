package cool.scx.web.vo;

import cool.scx.http.FileFormat;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * 原始文件 但不需要下载的 vo 比如 pdf 之类
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class Raw extends BaseWriter {

    private Raw(InputStream inputStream, FileFormat fileFormat) {
        super(inputStream, fileFormat.mediaType(), "inline");
    }

    private Raw(Path path) {
        super(path, null, "inline");
    }

    private Raw(byte[] bytes, FileFormat fileFormat) {
        super(bytes, fileFormat.mediaType(), "inline");
    }

    public static Raw of(byte[] bytes, FileFormat fileFormat) {
        return new Raw(bytes, fileFormat);
    }

    public static Raw of(Path path) {
        return new Raw(path);
    }

    public static Raw of(InputStream inputStream, FileFormat fileFormat) {
        return new Raw(inputStream, fileFormat);
    }

}
